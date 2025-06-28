package edu.npic.sps.features.user;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import edu.npic.sps.base.Status;
import edu.npic.sps.domain.*;
import edu.npic.sps.features.auth.EmailVerificationRepository;
import edu.npic.sps.features.gender.GenderRepository;
import edu.npic.sps.features.role.RoleRepository;
import edu.npic.sps.features.signUpMethod.SignUpMethodRepository;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.features.totp.TotpService;
import edu.npic.sps.features.user.dto.*;
import edu.npic.sps.mapper.UserMapper;
import edu.npic.sps.security.CustomUserDetails;
import edu.npic.sps.util.AuthUtil;
import edu.npic.sps.util.RandomOtp;
import edu.npic.sps.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final SimpMessagingTemplate simpMessageTemplate;
    private final TotpService totpService;
    private final AuthUtil authUtil;
    private final SignUpMethodRepository signUpMethodRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSenderImpl mailSender;
    private final GenderRepository genderRepository;
    private final TemplateEngine templateEngine;
    private final SiteRepository siteRepository;

    @Value("${file-server.server-path}")
    private String serverPath;

    @Value("${file-server.base-uri}")
    private String baseUri;

    @Value("${backend.url}")
    private String backendUrl;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(User user) {
        if (user.getPassword() != null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else {
            user.setPassword(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(12)));
        }
        return userRepository.save(user);
    }

    @Override
    public List<FullNameResponse> findAllFullName() {

        String loggedUserUuid = authUtil.loggedUserUuid();
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin =  authUtil.isAdminLoggedUser();
        List<String> verifiedUuid = authUtil.loggedUserSites();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<User> user = new ArrayList<>();

        if (isManager) {
            user = userRepository.findAll(sort);
        }else if(isAdmin) {
            user = userRepository.findAllFullNameBySite(loggedUserUuid ,verifiedUuid.stream().findFirst().orElseThrow());
        }

        return userMapper.toFullNameResponse(user);
    }

    @Override
    public IsOnlineResponse connectedUsers(String uuid, IsOnlineRequest isOnlineRequest) {
        System.out.println(isOnlineRequest.isOnline());
        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        user.setIsOnline(isOnlineRequest.isOnline());
        userRepository.save(user);

        userMapper.fromUpdateStatusRequest(isOnlineRequest, user);

        simpMessageTemplate.convertAndSend(
                "/topic/online-status",
                IsOnlineResponse.builder()
                        .uuid(uuid)
                        .isOnline(user.getIsOnline())
                        .build()
        );
        return IsOnlineResponse.builder()
                .uuid(uuid)
                .isOnline(user.getIsOnline())
                .build();
    }

    @Override
    public UserDetailResponse findByUuid(String uuid) {

        User user = userRepository.findByUuid(uuid).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found")
        );

//        return UserDetailResponse.builder()
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .gender(userMapper.toGenderReponse(user.getGender()))
//                .dateOfBirth(user.getDateOfBirth())
//                .id(user.getId())
//                .fullName(user.getFullName())
//                .email(user.getEmail())
//                .createdAt(user.getCreatedAt())
//                .phoneNumber(user.getPhoneNumber())
////                .isVerified(user.getIsVerified())
//                .status(user.getStatus())
//                .roleNames(user.getRoles().stream().map(Role::getName).toList())
//                .profileImage(user.getProfileImage())
//                .bannerImage(user.getBannerImage())
//                .build();
        return userMapper.toUserDetailResponse(user);
    }

    @Override
    public Page<UserDetailResponse> search(String keyword, String roleId, String status, String signUpMethodId, int pageNo, int pageSize, String branchId) {
        String loggedUserUuid = authUtil.loggedUserUuid();
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin =  authUtil.isAdminLoggedUser();
        List<String> verifiedUuid = authUtil.loggedUserSites();
        log.info("roleId: {}", status);

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        List<String> roleIds = null;
        if(roleId!=null && !roleId.isEmpty()){
            roleIds = Arrays.stream(roleId.split(","))
                    .map(String::trim)
                    .toList();
        }

        List<String> signUpMethodIds = null;
        if(signUpMethodId!=null && !signUpMethodId.isEmpty()){
            signUpMethodIds = Arrays.stream(signUpMethodId.split(","))
                    .map(String::trim)
                    .toList();
        }

        List<String> branchIds = null;
        if(branchId!=null && !branchId.isEmpty()){
            branchIds = Arrays.stream(branchId.split(","))
                    .map(String::trim)
                    .toList();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<User> users = Page.empty();

        if (isManager){
            if (!keyword.isEmpty() || roleIds != null || signUpMethodIds != null || !status.isEmpty() || branchIds != null || !branchIds.isEmpty()){
                users = userRepository.findUserByFilter(loggedUserUuid, keyword, roleIds, signUpMethodIds, status, branchIds, pageRequest);
            }
        }
//        else if(isAdmin){
//            if (!query.isEmpty()){
//                users = userRepository.findUserByRoleAdminSearchTerm(
//                        loggedUserUuid,
//                        verifiedUuid.stream().findFirst().orElseThrow(),
//                        query,
//                        pageRequest
//                );
//            }
//        }
        return users.map(userMapper::toUserDetailResponse);
    }

    @Override
    public void deleteByUuid(String uuid) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> sitesLogged = authUtil.loggedUserSites();

        if (isManager){
            User user = userRepository.findByUuid(uuid).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
            );

            if (emailVerificationRepository.existsByUser(user)){
                EmailVerification emailVerification = emailVerificationRepository.findByUser(user).orElseThrow();
                emailVerification.setUser(null);
            }

            userRepository.delete(user);
        }

    }

    @Override
    public UserDetailResponse updateUser(String uuid, UpdateUserRequest updateUserRequest) throws IOException, MessagingException {

        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> sitesLogged = authUtil.loggedUserSites();

        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found")
        );

        if (userRepository.existsByEmail(updateUserRequest.email()) && !user.getEmail().equals(updateUserRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        if (userRepository.existsByPhoneNumber(updateUserRequest.phoneNumber()) && !user.getPhoneNumber().equals(updateUserRequest.phoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }

        if(isManager){
            if (updateUserRequest.profileImage() != null) {
                deleteImageFile(user.getProfileImage());
            } else if (updateUserRequest.bannerImage() != null) {
                deleteImageFile(user.getBannerImage());
            }


            if (updateUserRequest.roleId() != null) {
                List<Role> newRoles = updateUserRequest.roleId().stream()
                        .map(roleUuid -> roleRepository.findByUuid(roleUuid)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")))
                        .toList();
                // Get the user's existing roles (if any)
                List<Role> existingRoles = user.getRoles();

                if(existingRoles != null) {
                    //Update the existing collection of roles
                    existingRoles.clear();
                    existingRoles.addAll(newRoles);
                } else {
                    user.setRoles(newRoles);
                }

            }

            if (updateUserRequest.genderId() != null) {
                Gender gender = genderRepository.findByUuid(updateUserRequest.genderId()).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Gender not found!"
                ));
                user.setGender(gender);
            }

            if (updateUserRequest.branchId() != null) {
                List<Site> newSites = updateUserRequest.branchId().stream()
                        .map(siteUuid -> siteRepository.findByUuid(siteUuid)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found!"))
                        ).toList();

                List<Site> existingSites = user.getSites();
                if(existingSites != null) {
                    existingSites.clear();
                    existingSites.addAll(newSites);
                } else {
                    user.setSites(newSites);
                }
            }

            if (updateUserRequest.isDeleted() != null){
                if (!updateUserRequest.isDeleted()){
                    user.setStatus(String.valueOf(Status.Active));

                }else{
                    user.setStatus(String.valueOf(Status.Banned));
                }
            }

            if (updateUserRequest.isVerified() != null){
                if (!updateUserRequest.isVerified()){
                    user.setStatus(String.valueOf(Status.Pending));

                    if (emailVerificationRepository.existsByUser(user)){
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "User not verify yet!");
                    }else {
                        sendEmailVerification(user, 15);
                    }
                }
            }
            userMapper.fromUpdateUserRequest(updateUserRequest, user);
            userRepository.save(user);
        }


        return userMapper.toUserDetailResponse(user);
    }

    @Override
    public void createUser(CreateUser createUser) throws MessagingException {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> sitesLogged = authUtil.loggedUserSites();

        if ( userRepository.existsByEmail(createUser.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        if (userRepository.existsByPhoneNumber(createUser.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }

        if (!createUser.password().equals(createUser.confirmPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        if (isManager){

        Gender gender = genderRepository.findByUuid(createUser.genderId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found")
        );

        List<Site> sites = createUser.branchId().stream().map(uuid -> siteRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
        )).toList();

        User user = userMapper.fromCreateUser(createUser);

        List<Role> roles = createUser.roleId().stream()
                .map(roleId -> roleRepository.findByUuid(roleId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")))
                .collect(Collectors.toList());

        user.setUuid(UUID.randomUUID().toString());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(createUser.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setStatus(String.valueOf(Status.Active));
        user.setSignUpMethod(signUpMethodRepository.findByName("CUSTOM").orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sign up method not found")));
        user.setIsOnline(false);
        user.setIsTwoFactorEnabled(false);
        user.setGender(gender);
        user.setSites(sites);
        user.setIsDeleted(!createUser.isVerified());
        userRepository.save(user);

        if (!createUser.isVerified()){
            sendEmailVerification(user, 30);
        }

        }else if (isAdmin){

            Gender gender = genderRepository.findByUuid(createUser.genderId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found")
            );

            List<Site> sites = sitesLogged.stream().map(uuid -> siteRepository.findByUuid(uuid).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.CREATED, "Branch not found")
            )).toList();

            User user = userMapper.fromCreateUser(createUser);
            List<Role> roles = roleRepository.findAll().stream().filter(role -> role.getName().equalsIgnoreCase("USER")).toList();
            user.setUuid(UUID.randomUUID().toString());
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(createUser.password()));
            user.setCreatedAt(LocalDateTime.now());
            user.setIsAccountNonLocked(true);
            user.setIsAccountNonExpired(true);
            user.setIsCredentialsNonExpired(true);
            user.setStatus(String.valueOf(Status.Active));
            user.setSignUpMethod(signUpMethodRepository.findByName("CUSTOM").orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sign up method not found")));
            user.setIsOnline(false);
            user.setIsTwoFactorEnabled(false);
            user.setGender(gender);
            user.setSites(sites);
            user.setIsDeleted(!createUser.isVerified());
            userRepository.save(user);

            if (!createUser.isVerified()){
//                EmailVerification emailVerification = new EmailVerification();
//                emailVerification.setEmail(user.getEmail());
//                emailVerification.setExpiryTime(LocalTime.now().plusMinutes(1));
//                emailVerification.setUser(user);
//                emailVerification.setVerificationCode(RandomOtp.generateSecurityCode());
//                emailVerification.setToken(RandomUtil.randomUuidToken());
//                emailVerificationRepository.save(emailVerification);
//
//                String verifyUrl = backendUrl + "/verify?token=" + emailVerification.getToken();
//                Context context = new Context();
//                context.setVariable("email", emailVerification.getEmail());
//                context.setVariable("expire", emailVerification.getExpiryTime());
//                context.setVariable("tokenUrl", verifyUrl);
//                String htmlContent = templateEngine.process("emailTemplate", context);
//
//                MimeMessage mimeMessage = mailSender.createMimeMessage();
//                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//                helper.setSubject("Email Verification - SPS");
//                helper.setTo(user.getEmail());
//                helper.setFrom(adminMail);
//                helper.setText(htmlContent, true);
//
//                mailSender.send(mimeMessage);
            }
        }
    }

    @Override
    public GetAllUserResponse findAll(int pageNo, int pageSize) {
        String loggedUserUuid = authUtil.loggedUserUuid();
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin =  authUtil.isAdminLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<User> users = Page.empty();
        Map<String, Integer> statusCount = new HashMap<>();

            if (isManager){
                    users = userRepository.findByUuidNot(loggedUserUuid, pageRequest);
                    statusCount.put("Active", userRepository.countActiveUser(loggedUserUuid));
                    statusCount.put("Pending", userRepository.countPendingUser());
                    statusCount.put("Banned", userRepository.countBannedUser());
            }
            else if(isAdmin){
                    users = userRepository.findUserByRoleAdmin(
                            loggedUserUuid ,
                            sites.stream().findFirst().orElseThrow(),
                            pageRequest);
                statusCount.put("Active", userRepository.countActiveUserBySite(loggedUserUuid, sites.stream().findFirst().orElseThrow()));
                statusCount.put("Pending", userRepository.countPendingUserBySite(sites.stream().findFirst().orElseThrow()));
                statusCount.put("Banned", userRepository.countBannedUserBySite(sites.stream().findFirst().orElseThrow()));
            }


        Page<UserDetailResponse> userDetailResponses= users.map(userMapper::toUserDetailResponse);
        return GetAllUserResponse.builder()
                .allUsers(userDetailResponses)
                .statusCount(statusCount)
                .build();
    }

    @Override
    public void register(CreateUserRegister createUserRegister) {

        if (userRepository.existsByEmail(createUserRegister.email())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists!"
            );
        }

        if (!createUserRegister.password().equals(createUserRegister.confirmPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords and confirm passwords do not match!"
            );
        }

        User user = userMapper.fromCreateUserRegister(createUserRegister);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsVerified(false);
        user.setPassword(passwordEncoder.encode(createUserRegister.password()));
        user.setProfileImage("default-avatar.png");
        user.setCreatedAt(LocalDateTime.now());
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsDeleted(false);

        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("STAFF").build());
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public GoogleAuthenticatorKey generate2FASecret(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        GoogleAuthenticatorKey key = totpService.generateSecret();
        user.setTwoFactorSecret(key.getKey());
        userRepository.save(user);
        return key;
    }

    @Override
    public boolean validate2FACode(Integer userId, int code){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        return totpService.verifyCode(user.getTwoFactorSecret(), code);
    }

    @Override
    public void enable2FA(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        user.setIsTwoFactorEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void disable2FA(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        user.setIsTwoFactorEnabled(false);
        userRepository.save(user);
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
    }

    @Override
    public ResponseEntity<?> find2faStatus() {
        User user = authUtil.loggedUser();
        if(user != null){
            return ResponseEntity.ok().body(Map.of("is2faEnabled", user.getIsTwoFactorEnabled()));
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    @Override
    public ResponseEntity<?> find2faSecretCode() {
        User user = authUtil.loggedUser();
        if(user != null){
            return ResponseEntity.ok().body(Map.of("twoFASecretCode", user.getTwoFactorSecret()));
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    private void deleteImageFile(String imageUrl) throws IOException {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            Path path = Path.of(serverPath + fileName);
            Files.deleteIfExists(path);
        }
    }

    private void sendEmailVerification(User user, Integer expireMinute) throws MessagingException {
//        EmailVerification emailVerification = new EmailVerification();
//        emailVerification.setEmail(user.getEmail());
//        emailVerification.setExpiryTime(LocalTime.now().plusMinutes(expireMinute));
//        emailVerification.setUser(user);
//        emailVerification.setVerificationCode(RandomOtp.generateSecurityCode());
//        emailVerification.setToken(RandomUtil.randomUuidToken());
//        emailVerificationRepository.save(emailVerification);
//
//        String verifyUrl = backendUrl + "/verify?token=" + emailVerification.getToken();
//        Context context = new Context();
//        context.setVariable("email", emailVerification.getEmail());
//        context.setVariable("expire", emailVerification.getExpiryTime());
//        context.setVariable("tokenUrl", verifyUrl);
//        String htmlContent = templateEngine.process("emailTemplate", context);
//
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//        helper.setSubject("Email Verification - SPS");
//        helper.setTo(user.getEmail());
//        helper.setFrom(adminMail);
//        helper.setText(htmlContent, true);
//
//        mailSender.send(mimeMessage);
    }

}
