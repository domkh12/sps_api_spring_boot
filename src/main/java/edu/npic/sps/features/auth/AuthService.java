package edu.npic.sps.features.auth;

import edu.npic.sps.features.auth.dto.*;
import edu.npic.sps.features.user.dto.CreateUserRegister;
import edu.npic.sps.features.user.dto.UpdateProfileUserRequest;
import edu.npic.sps.features.user.dto.UserDetailResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    MessageResponse resetPassword(ResetPasswordRequest resetPasswordRequest);

    MessageResponse forgotPassword(String email);

    ResponseEntity<Void> logout(HttpServletResponse response);

    ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<JwtResponse> login(LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request);

    void register(CreateUserRegister createUserRegister) throws MessagingException;

    void verify(VerifyRequest verifyRequest, String token);

    QrCodeResponse enable2FA();

    MessageResponse disable2FA();

    MessageResponse verify2FA(Integer code);

    JwtResponse verify2FALogin(Integer code, String email, HttpServletResponse response);

    UserDetailResponse getProfileUser();

    JwtResponse verifySites(String uuid, HttpServletResponse response);

    UserDetailResponse updateProfileUser(UpdateProfileUserRequest updateProfileRequest);

    void changePassword(ChangePasswordRequest changePasswordRequest);
}
