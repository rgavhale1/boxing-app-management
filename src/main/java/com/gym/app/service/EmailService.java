package com.gym.app.service;

import com.gym.app.model.JoinBatchRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String toEmail, String name, String program) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(toEmail);
        helper.setSubject("Gym Registration Confirmed");

        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>Gym Registration Confirmed</title>
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
                <tr>
                  <td style="background-color: #27ae60; color: #ffffff; padding: 20px; text-align: center;">
                    <h2 style="margin: 0;">✅ Registration Successful</h2>
                  </td>
                </tr>
                <tr>
                  <td style="padding: 20px; color: #333333;">
                    <p>Hi <strong>%s</strong>,</p>
                    <p>We’re excited to let you know that your registration for the <strong>%s</strong> program has been confirmed!</p>
                    
                    <p style="margin-top: 15px;">Our team will contact you soon with further details. Meanwhile, get ready to achieve your fitness goals with us.</p>
                    
                    <p style="margin-top: 20px;">Thanks,<br><strong>Gym Team</strong></p>
                  </td>
                </tr>
                <tr>
                  <td style="background-color: #27ae60; color: #ffffff; text-align: center; padding: 10px;">
                    <p style="margin: 0;">Stay Fit • Stay Strong</p>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """;

        String htmlMessage = String.format(htmlContent, name, program);

        helper.setText(htmlMessage, true); // true = send as HTML
        mailSender.send(mimeMessage);
    }


    public void sendAdminNotification(String adminEmail, JoinBatchRequest request) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(adminEmail);
        helper.setSubject("New Gym Registration - " + request.getProgram());

        String htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>New Gym Registration</title>
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
                <tr>
                  <td style="background-color: #2c3e50; color: #ffffff; padding: 20px; text-align: center;">
                    <h2 style="margin: 0;">🏋️ Gym Registration Alert</h2>
                  </td>
                </tr>
                <tr>
                  <td style="padding: 20px; color: #333333;">
                    <p>Hello Admin,</p>
                    <p>A new user has joined one of your gym programs. Here are the details:</p>
                            
                    <table width="100%%" cellpadding="8" cellspacing="0" style="border-collapse: collapse; margin-top: 10px;">
                      <tr style="background-color: #ecf0f1;">
                        <td><strong>Name:</strong></td>
                        <td>%s</td>
                      </tr>
                      <tr>
                        <td><strong>Email:</strong></td>
                        <td>%s</td>
                      </tr>
                      <tr style="background-color: #ecf0f1;">
                        <td><strong>Mobile:</strong></td>
                        <td>%s</td>
                      </tr>
                      <tr>
                        <td><strong>Program:</strong></td>
                        <td>%s</td>
                      </tr>
                      <tr style="background-color: #ecf0f1;">
                        <td><strong>Batch Time:</strong></td>
                        <td>%s</td>
                      </tr>
                    </table>
                            
                    <p style="margin-top: 20px;">Please follow up with the user to confirm their schedule and next steps.</p>
                    <p style="color: #888888; font-size: 12px;">This is an automated notification from the Gym Registration System.</p>
                  </td>
                </tr>
                <tr>
                  <td style="background-color: #2c3e50; color: #ffffff; text-align: center; padding: 10px;">
                    <p style="margin: 0;">© 2026 Gym Team</p>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """;

        String htmlContent = String.format(
                htmlTemplate,
                request.getName(),
                request.getEmail(),
                request.getMobile(),
                request.getProgram(),
                request.getTime()
        );

        helper.setText(htmlContent, true); // true = send as HTML
        mailSender.send(mimeMessage);
    }

}