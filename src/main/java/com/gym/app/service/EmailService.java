package com.gym.app.service;

import com.gym.app.model.JoinBatchRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // ✅ Must be VERIFIED in SendGrid (Single Sender or Domain Auth)
    private static final String FROM_EMAIL = "rgavhale1994@gmail.com";
    private static final String FROM_NAME = "Boxing Avenue";

    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // ✅ SendGrid requires verified sender
        helper.setFrom(FROM_EMAIL, FROM_NAME);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        try {
            mailSender.send(mimeMessage);
            log.info("✅ Email sent to: {}", to);
        } catch (Exception e) {
            log.error("❌ Failed to send email to {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }

    public void sendConfirmationEmail(String toEmail, String name, String program) throws MessagingException, UnsupportedEncodingException {

        String htmlMessage = String.format("""
            <html>
            <body>
              <h2 style="color:green;">Registration Successful</h2>
              <p>Hi <b>%s</b>,</p>
              <p>Your registration for <b>%s</b> is confirmed.</p>
              <p>Thanks,<br>Gym Team</p>
            </body>
            </html>
        """, name, program);

        sendEmail(toEmail, "Gym Registration Confirmed", htmlMessage);
    }

    public void sendAdminNotification(String adminEmail, JoinBatchRequest request) throws MessagingException, UnsupportedEncodingException {

        String htmlMessage = String.format("""
            <html>
            <body>
              <h2>New Registration</h2>
              <p><b>Name:</b> %s</p>
              <p><b>Email:</b> %s</p>
              <p><b>Mobile:</b> %s</p>
              <p><b>Program:</b> %s</p>
              <p><b>Time:</b> %s</p>
            </body>
            </html>
        """,
                request.getName(),
                request.getEmail(),
                request.getMobile(),
                request.getProgram(),
                request.getTime()
        );

        sendEmail(adminEmail, "New Gym Registration", htmlMessage);
    }

    public void sendResetPasswordEmail(String toEmail, String name, String resetLink) throws MessagingException, UnsupportedEncodingException {

        String htmlMessage = String.format("""
            <html>
            <body>
              <h2>Password Reset</h2>
              <p>Hi <b>%s</b>,</p>
              <p>Click below to reset your password:</p>
              <a href="%s">Reset Password</a>
              <p>If not requested, ignore.</p>
            </body>
            </html>
        """, name, resetLink);

        sendEmail(toEmail, "Password Reset Request", htmlMessage);
    }
}