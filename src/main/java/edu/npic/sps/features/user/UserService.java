package edu.npic.sps.features.user;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import edu.npic.sps.domain.User;
import edu.npic.sps.features.user.dto.*;
import jakarta.mail.MessagingException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    ResponseEntity<InputStreamResource> reportUserXlsx() throws IOException;

    ResponseEntity<InputStreamResource> reportUserPdf() throws IOException;

    Page<ReportUserResponse> report(int pageNo, int pageSize, LocalDateTime dateFrom, LocalDateTime dateTo);

    Optional<User> findByEmail(String email);

    User registerUser(User user);

    List<FullNameResponse> findAllFullName();

    IsOnlineResponse connectedUsers(String uuid, IsOnlineRequest isOnlineRequest);

    UserDetailResponse findByUuid(String uuid);

    Page<UserDetailResponse> search(String keyword, String roleId, String status, String signUpMethodId, int pageNo, int pageSize, String branchId);

    void deleteByUuid(String uuid);

    UserDetailResponse updateUser(String uuid, UpdateUserRequest updateUserRequest) throws IOException, MessagingException;

    void createUser(CreateUser createUser) throws MessagingException;

    GetAllUserResponse findAll(int pageNo, int pageSize);

    void register(CreateUserRegister createUserRegister);

    GoogleAuthenticatorKey generate2FASecret(Integer userId);

    boolean validate2FACode(Integer userId, int code);

    void enable2FA(Integer userId);

    void disable2FA(Integer userId);

    User getUserById(Integer userId);

    ResponseEntity<?> find2faStatus();

    ResponseEntity<?> find2faSecretCode();
}
