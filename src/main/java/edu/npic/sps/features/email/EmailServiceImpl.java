package edu.npic.sps.features.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    @Override
    public void sendPasswordResetEmail(String email, String token) {
        Context context = new Context();
        context.setVariable("token", token);

        String htmlContent = templateEngine.process("resetPassword", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(email);
            helper.setSubject("Reset Password");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
