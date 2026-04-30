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
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <title>Gym Registration Confirmed</title>
        </head>

        <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">

          <table width="100%%" cellpadding="0" cellspacing="0"
                 style="max-width: 600px; margin: auto;
                 background-color: #ffffff; border-radius: 8px;
                 overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">

            <!-- HEADER -->
            <tr>
              <td style="background-color: #28a745; color: #ffffff;
                         padding: 20px; text-align: center;">
                <h2 style="margin: 0;">✅ Registration Successful</h2>
              </td>
            </tr>

            <!-- BODY -->
            <tr>
              <td style="padding: 20px; color: #333333;">

                <p>Hi <strong>%s</strong>,</p>

                <p>
                  Your registration for the
                  <strong>%s</strong> program has been successfully confirmed.
                </p>

                <p style="margin-top: 15px;">
                  We’re excited to have you join our fitness journey.
                  Our team will contact you soon with further details.
                </p>

                <p style="margin-top: 20px;">
                  Thanks,<br>
                  <strong>Gym Team</strong>
                </p>

              </td>
            </tr>

            <!-- FOOTER -->
            <tr>
              <td style="background-color: #28a745;
                         color: #ffffff;
                         text-align: center;
                         padding: 10px;">
                <p style="margin: 0;">Stay Fit • Stay Strong</p>
              </td>
            </tr>

          </table>

        </body>
        </html>
        """, name, program);

        sendEmail(toEmail, "Gym Registration Confirmed", htmlMessage);
    }
    public void sendAdminNotification(String adminEmail, JoinBatchRequest request) {

        String htmlTemplate = """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <title>New Gym Registration</title>
        </head>

        <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">

          <table width="100%%" cellpadding="0" cellspacing="0"
                 style="max-width: 600px; margin: auto;
                 background-color: #ffffff; border-radius: 8px;
                 overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">

            <!-- HEADER -->
            <tr>
              <td style="background-color: #2c3e50; color: #ffffff;
                         padding: 20px; text-align: center;">
                <h2 style="margin: 0;">🏋️ New Gym Registration Alert</h2>
              </td>
            </tr>

            <!-- BODY -->
            <tr>
              <td style="padding: 20px; color: #333333;">

                <p>Hello Admin,</p>

                <p>
                  A new user has registered for your gym program.
                  Below are the details:
                </p>

                <!-- DETAILS TABLE -->
                <table width="100%%" cellpadding="10" cellspacing="0"
                       style="border-collapse: collapse; margin-top: 15px; width: 100%%;">

                  <tr style="background-color: #f1f1f1;">
                    <td><strong>Name</strong></td>
                    <td>%s</td>
                  </tr>

                  <tr>
                    <td><strong>Email</strong></td>
                    <td>%s</td>
                  </tr>

                  <tr style="background-color: #f1f1f1;">
                    <td><strong>Mobile</strong></td>
                    <td>%s</td>
                  </tr>

                  <tr>
                    <td><strong>Program</strong></td>
                    <td>%s</td>
                  </tr>

                  <tr style="background-color: #f1f1f1;">
                    <td><strong>Batch Time</strong></td>
                    <td>%s</td>
                  </tr>

                </table>

                <p style="margin-top: 20px;">
                  Please follow up with the user for confirmation and onboarding.
                </p>

                <p style="font-size: 12px; color: #888;">
                  This is an automated notification from the Gym Registration System.
                </p>

              </td>
            </tr>

            <!-- FOOTER -->
            <tr>
              <td style="background-color: #2c3e50;
                         color: #ffffff;
                         text-align: center;
                         padding: 10px;">
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

        sendEmail(adminEmail, "New Gym Registration - " + request.getProgram(), htmlContent);
    }
    public void sendResetPasswordEmail(String toEmail, String name, String resetLink) {

        String htmlMessage = String.format("""
        <!DOCTYPE html>
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">

          <div style="max-width: 600px; margin: auto; background: #ffffff;
                      padding: 20px; border-radius: 8px;
                      box-shadow: 0 2px 6px rgba(0,0,0,0.1);">

            <div style="background-color: #28a745; color: white;
                        padding: 15px; text-align: center;
                        border-radius: 6px;">
              <h2 style="margin: 0;">🔒 Password Reset</h2>
            </div>

            <p style="margin-top: 20px;">Hi <b>%s</b>,</p>

            <p>
              We received a request to reset your password.
              Click the button below to continue:
            </p>

            <p style="text-align: center; margin: 25px 0;">
              <a href="%s"
                 style="background-color: #28a745;
                        color: white;
                        padding: 12px 18px;
                        text-decoration: none;
                        border-radius: 5px;
                        font-weight: bold;">
                Reset Password
              </a>
            </p>

            <p>
              If you did not request this, you can safely ignore this email.
            </p>

            <p style="margin-top: 20px;">
              Thanks,<br>
              <b>Gym Team</b>
            </p>

          </div>

        </body>
        </html>
        """, name, resetLink);

        sendEmail(toEmail, "Password Reset Request", htmlMessage);
    }
}