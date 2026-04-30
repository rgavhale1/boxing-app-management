package com.gym.app.service;

import com.gym.app.model.JoinBatchRequest;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    // ✅ Your VERIFIED sender email
    private static final String FROM_EMAIL = "noreply@boxingave.com";
    private static final String FROM_NAME = "Boxing Avenue";

    private void sendEmail(String to, String subject, String htmlContent) {

        try {
            Email from = new Email(FROM_EMAIL, FROM_NAME);
            Email toEmail = new Email(to);

            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, toEmail, content);

            SendGrid sg = new SendGrid(apiKey);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            log.info("✅ Email sent. Status Code: {}", response.getStatusCode());

        } catch (Exception e) {
            log.error("❌ Email sending failed: {}", e.getMessage(), e);
        }
    }

    public void sendConfirmationEmail(String toEmail, String name, String program) {

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

    public void sendAdminNotification(String adminEmail, JoinBatchRequest request) {

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

    public void sendResetPasswordEmail(String toEmail, String name, String resetLink) {

        String htmlMessage = String.format("""
            <html>
            <body>
              <h2>Password Reset</h2>
              <p>Hi <b>%s</b>,</p>
              <p>Click below to reset your password:</p>
              <a href="%s">Reset Password</a>
              <p>If not requested, ignore this email.</p>
            </body>
            </html>
        """, name, resetLink);

        sendEmail(toEmail, "Password Reset Request", htmlMessage);
    }
}