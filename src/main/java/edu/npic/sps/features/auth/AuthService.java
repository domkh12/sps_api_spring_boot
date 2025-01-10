package edu.npic.sps.features.auth;

import edu.npic.sps.features.auth.dto.*;
import edu.npic.sps.features.user.dto.CreateUserRegister;
import edu.npic.sps.features.user.dto.UserDetailResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<Void> logout(HttpServletResponse response);

    ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<JwtResponse> login(LoginRequest loginRequest, HttpServletResponse response);

    void register(CreateUserRegister createUserRegister) throws MessagingException;

    void verify(VerifyRequest verifyRequest, String token);

    QrCodeResponse enable2FA();

    MessageResponse disable2FA();

    MessageResponse verify2FA(Integer code);

    MessageResponse verify2FALogin(Integer code, String token,HttpServletResponse response);

    UserDetailResponse getProfileUser();

    MessageResponse verifySites(String uuid, String token, HttpServletResponse response);
}
