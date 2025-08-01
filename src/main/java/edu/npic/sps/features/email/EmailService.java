package edu.npic.sps.features.email;

public interface EmailService {

    void sendPasswordResetEmail(String email, String token);

}
