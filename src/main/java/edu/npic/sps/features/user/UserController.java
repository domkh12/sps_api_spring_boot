package edu.npic.sps.features.user;

import edu.npic.sps.features.user.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/full-names")
    List<FullNameResponse> findAllFullName (){
        return userService.findAllFullName();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PatchMapping("/{uuid}/status")
    IsOnlineResponse connectedUsers (@PathVariable String uuid, @Valid @RequestBody IsOnlineRequest isOnlineRequest){
       return userService.connectedUsers(uuid, isOnlineRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{uuid}")
    UserDetailResponse findByUuid(@PathVariable String uuid) {
        return userService.findByUuid(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    Page<UserDetailResponse> search( @RequestParam(required = false, defaultValue = "") String keyword,
                                     @RequestParam(required = false, defaultValue = "") String roleId,
                                     @RequestParam(required = false, defaultValue = "") String status,
                                     @RequestParam(required = false, defaultValue = "") String signUpMethodId,
                                     @RequestParam(required = false, defaultValue = "") String branchId,
                                     @RequestParam(required = false, defaultValue = "1") int pageNo,
                                     @RequestParam(required = false, defaultValue = "20") int pageSize){
        return userService.search(keyword, roleId, status, signUpMethodId, pageNo, pageSize, branchId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void deleteByUuid(@PathVariable String uuid){
        userService.deleteByUuid(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    UserDetailResponse updateUser(@PathVariable String uuid, @Valid @RequestBody UpdateUserRequest updateUserRequest) throws IOException, MessagingException {
        return userService.updateUser(uuid, updateUserRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createUser( @Valid @RequestBody CreateUser createUser) throws MessagingException {
        userService.createUser(createUser);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registers")
    void register(@Valid @RequestBody CreateUserRegister createUserRegister){
        userService.register(createUserRegister);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    GetAllUserResponse findAll (
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize
            ){
        return userService.findAll(pageNo, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/2fa-status")
    ResponseEntity<?> find2faStatus(){
        return userService.find2faStatus();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/2fa-secret-code")
    ResponseEntity<?> find2faSecretCode(){
        return userService.find2faSecretCode();
    }
}
