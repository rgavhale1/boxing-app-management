package com.boxing.app.service;

import com.boxing.app.model.JoinBatchRequest;
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

    public void sendConfirmationEmail(String toEmail, String name, String programType, String service) {

        // ── Parse programType string ──────────────────────────────────────────
        // Line 0 → program name
        // Line 1 → current price (e.g. "₹20000 / MONTH")
        // Line 2 → features
        // Line 3 → original price  (optional)
        // Line 4 → discount label  (optional)
        String[] parts = programType.split("\\n", -1);

        String progName      = parts.length > 0 ? parts[0].trim() : "—";
        String currentPrice  = parts.length > 1 ? parts[1].trim().replace("/ MONTH", "").trim() : "—";
        String features      = parts.length > 2 ? parts[2].trim() : "—";
        String originalPrice = parts.length > 3 ? parts[3].trim() : null;
        String discount      = parts.length > 4 ? parts[4].trim() : null;

        // ── Discount badge ────────────────────────────────────────────────────
        String discountBadge = (discount != null && !discount.isEmpty()) ? String.format("""
        <span style="
            display:inline-block;
            background:linear-gradient(135deg,#ff3c3c,#cc0000);
            color:#fff;
            font-size:11px;
            font-weight:800;
            padding:4px 10px;
            border-radius:4px;
            margin-left:10px;
            letter-spacing:1px;
            vertical-align:middle;
            text-transform:uppercase;
            box-shadow:0 2px 8px rgba(255,60,60,0.45);
        ">%s</span>
    """, discount) : "";

        // ── Price line ────────────────────────────────────────────────────────
        String priceLine;
        if (originalPrice != null && !originalPrice.isEmpty()) {
            priceLine = String.format("""
            <span style="text-decoration:line-through;color:#555;font-size:14px;font-weight:500;margin-right:6px;">%s</span><span style="color:#ffc542;font-size:22px;font-weight:800;letter-spacing:-0.5px;">%s</span><span style="color:#777;font-size:13px;margin-left:4px;">/ MONTH</span>%s
        """, originalPrice, currentPrice, discountBadge);
        } else {
            priceLine = String.format("""
            <span style="color:#ffc542;font-size:22px;font-weight:800;letter-spacing:-0.5px;">%s</span><span style="color:#777;font-size:13px;margin-left:4px;">/ MONTH</span>
        """, currentPrice);
        }

        // ── Feature pills ─────────────────────────────────────────────────────
        String[] featureList = features.split("\\+");
        StringBuilder featurePills = new StringBuilder();
        for (String f : featureList) {
            featurePills.append(String.format("""
            <span style="
                display:inline-block;
                background:rgba(255,197,66,0.08);
                border:1px solid rgba(255,197,66,0.2);
                color:#aaa;
                font-size:10px;
                font-weight:700;
                letter-spacing:1.2px;
                padding:3px 10px;
                border-radius:20px;
                margin:3px 3px 3px 0;
                text-transform:uppercase;
            ">%s</span>
        """, f.trim()));
        }

        // ── Full email ────────────────────────────────────────────────────────
        String htmlMessage = String.format("""
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8"/>
          <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
          <title>Avenue — Registration Confirmed</title>
        </head>
        <body style="margin:0;padding:0;background:#0a0a0a;font-family:'Segoe UI',Arial,sans-serif;">

          <table width="100%%" cellpadding="0" cellspacing="0" style="background:#0a0a0a;padding:40px 16px;">
            <tr><td align="center">
              <table width="580" cellpadding="0" cellspacing="0" style="max-width:580px;width:100%%;">

                <!-- ══ TOP ACCENT BAR ══ -->
                <tr>
                  <td style="
                    background:linear-gradient(90deg,#ffc542,#ff5f5f,#ffc542);
                    height:4px;
                    border-radius:4px 4px 0 0;
                  "></td>
                </tr>

                <!-- ══ HEADER ══ -->
                <tr>
                  <td style="
                    background:linear-gradient(160deg,#181818 0%%,#111 100%%);
                    padding:44px 48px 36px;
                    text-align:center;
                    border-left:1px solid #222;
                    border-right:1px solid #222;
                  ">
                    <!-- Logo / Brand -->
                    <div style="margin-bottom:24px;">
                      <span style="
                        font-size:13px;
                        font-weight:800;
                        letter-spacing:6px;
                        color:#ffc542;
                        text-transform:uppercase;
                      ">BOXING AVENUE</span>
                    </div>

                    <!-- Big checkmark -->
                    <div style="
                      display:inline-block;
                      background:linear-gradient(135deg,#1e3a1e,#0f2a0f);
                      border:2px solid #2d6a2d;
                      border-radius:50%%;
                      width:68px;
                      height:68px;
                      line-height:68px;
                      font-size:30px;
                      margin-bottom:20px;
                      box-shadow:0 0 32px rgba(45,106,45,0.4);
                    ">✅</div>

                    <h1 style="
                      margin:0 0 8px;
                      font-size:26px;
                      font-weight:800;
                      color:#fff;
                      letter-spacing:1px;
                      text-transform:uppercase;
                    ">Registration Successful</h1>
                    <p style="margin:6px 0 0;color:#ddd;font-size:15px;letter-spacing:0.3px;line-height:1.8;">
                      Hi <strong style="color:#ffc542;font-size:17px;">%s</strong>,<br/>
                      <span style="color:#fff;font-weight:600;">Your registration has been successfully confirmed for:</span>
                    </p>
                  </td>
                </tr>

                <!-- ══ BODY ══ -->
                <tr>
                  <td style="
                    background:#141414;
                    padding:36px 48px;
                    border-left:1px solid #222;
                    border-right:1px solid #222;
                  ">

                    <!-- Service label -->
                    <div style="margin-bottom:8px;">
                      <span style="
                        font-size:10px;
                        font-weight:700;
                        letter-spacing:2px;
                        color:#555;
                        text-transform:uppercase;
                      ">SERVICE</span>
                    </div>
                    <div style="
                      background:#1a1a1a;
                      border:1px solid #2a2a2a;
                      border-radius:8px;
                      padding:12px 16px;
                      margin-bottom:28px;
                      color:#e0e0e0;
                      font-size:14px;
                      font-weight:600;
                    ">%s</div>

                    <!-- ── PROGRAM CARD ── -->
                    <div style="margin-bottom:8px;">
                      <span style="
                        font-size:10px;
                        font-weight:700;
                        letter-spacing:2px;
                        color:#555;
                        text-transform:uppercase;
                      ">YOUR PROGRAM</span>
                    </div>

                    <table width="100%%" cellpadding="0" cellspacing="0" style="
                      background:linear-gradient(135deg,#1c1c1c,#181818);
                      border:1px solid #2e2e2e;
                      border-radius:14px;
                      overflow:hidden;
                      margin-bottom:32px;
                      box-shadow:0 8px 40px rgba(0,0,0,0.5);
                    ">
                      <!-- Card top accent -->
                      <tr>
                        <td style="
                          background:linear-gradient(90deg,#ffc54222,#ff5f5f11,transparent);
                          height:2px;
                        "></td>
                      </tr>
                      <tr>
                        <td style="padding:24px 28px;">

                          <!-- Program name -->
                          <div style="
                            font-size:16px;
                            font-weight:800;
                            color:#fff;
                            letter-spacing:0.3px;
                            margin-bottom:12px;
                          ">%s</div>

                          <!-- Price row -->
                          <div style="margin-bottom:16px;line-height:1;">
                            %s
                          </div>

                          <!-- Divider -->
                          <div style="
                            height:1px;
                            background:linear-gradient(90deg,#2a2a2a,transparent);
                            margin-bottom:14px;
                          "></div>

                          <!-- Feature pills -->
                          <div>%s</div>

                        </td>
                      </tr>
                    </table>

                    <!-- ══ CONTACT US SECTION ══ -->
                    <table width="100%%" cellpadding="0" cellspacing="0" style="
                      background:#0d0d0d;
                      border:1px solid #222;
                      border-radius:12px;
                      overflow:hidden;
                      margin-bottom:0;
                    ">
                      <!-- Section accent top -->
                      <tr>
                        <td style="
                          background:linear-gradient(90deg,#ffc54233,transparent);
                          height:2px;
                        "></td>
                      </tr>

                      <tr>
                        <td style="padding:24px 28px;">

                          <!-- Contact Us heading -->
                          <div style="
                            font-size:11px;
                            font-weight:800;
                            letter-spacing:3px;
                            color:#ffc542;
                            text-transform:uppercase;
                            margin-bottom:14px;
                          ">📬 &nbsp;Contact Us</div>

                          <!-- Email -->
                          <div style="margin-bottom:8px;display:flex;align-items:center;">
                            <span style="font-size:14px;margin-right:8px;">📧</span>
                            <a href="mailto:boxingavenueofficial@gmail.com" style="
                              color:#ccc;
                              font-size:13px;
                              font-weight:500;
                              text-decoration:none;
                            ">boxingavenueofficial@gmail.com</a>
                          </div>

                          <!-- Phone -->
                          <div style="margin-bottom:20px;">
                            <span style="font-size:14px;margin-right:8px;">📱</span>
                            <a href="tel:+919307065559" style="
                              color:#ccc;
                              font-size:13px;
                              font-weight:500;
                              text-decoration:none;
                            ">+91 93070 65559</a>
                          </div>

                          <!-- Divider -->
                          <div style="
                            height:1px;
                            background:linear-gradient(90deg,#2a2a2a,transparent);
                            margin-bottom:18px;
                          "></div>

                          <!-- Address heading -->
                          <div style="
                            font-size:11px;
                            font-weight:800;
                            letter-spacing:3px;
                            color:#ffc542;
                            text-transform:uppercase;
                            margin-bottom:12px;
                          ">📍 &nbsp;Address</div>

                          <div style="color:#bbb;font-size:13px;line-height:1.9;margin-bottom:20px;">
                            <strong style="color:#fff;font-size:14px;font-weight:700;">Boxing Avenue</strong><br/>
                            3rd Floor, above ALPHA FIT gym<br/>
                            Dharampeth Square, Gorepeth<br/>
                            Nagpur, Maharashtra 440010
                          </div>

                          <!-- Divider -->
                          <div style="
                            height:1px;
                            background:linear-gradient(90deg,#2a2a2a,transparent);
                            margin-bottom:18px;
                          "></div>

                          <!-- Follow heading -->
                          <div style="
                            font-size:11px;
                            font-weight:800;
                            letter-spacing:3px;
                            color:#ffc542;
                            text-transform:uppercase;
                            margin-bottom:14px;
                          ">🌐 &nbsp;Follow Us</div>

                          <!-- Social pills -->
                          <div>
                            <a href="https://www.instagram.com/boxing.ave?igsh=MTJjZjZwYmdwMmRxcw%%3D%%3D&utm_source=qr" style="
                              display:inline-block;
                              background:linear-gradient(135deg,#833ab4,#fd1d1d,#fcb045);
                              color:#fff;
                              font-size:11px;
                              font-weight:700;
                              letter-spacing:1px;
                              padding:6px 16px;
                              border-radius:20px;
                              text-decoration:none;
                              margin:3px 4px 3px 0;
                              text-transform:uppercase;
                            ">📸 Instagram</a>
                            <a href="https://www.youtube.com/@Boxingavenue" style="
                              display:inline-block;
                              background:linear-gradient(135deg,#c4302b,#991f1b);
                              color:#fff;
                              font-size:11px;
                              font-weight:700;
                              letter-spacing:1px;
                              padding:6px 16px;
                              border-radius:20px;
                              text-decoration:none;
                              margin:3px 4px 3px 0;
                              text-transform:uppercase;
                            ">▶ YouTube</a>
                            <a href="https://wa.link/jgzjnw" style="
                              display:inline-block;
                              background:linear-gradient(135deg,#25d366,#128c4a);
                              color:#fff;
                              font-size:11px;
                              font-weight:700;
                              letter-spacing:1px;
                              padding:6px 16px;
                              border-radius:20px;
                              text-decoration:none;
                              margin:3px 4px 3px 0;
                              text-transform:uppercase;
                            ">💬 WhatsApp</a>
                          </div>

                        </td>
                      </tr>
                    </table>

                  </td>
                </tr>

                <!-- ══ FOOTER ══ -->
                <tr>
                  <td style="
                    background:#0f0f0f;
                    border:1px solid #1a1a1a;
                    border-top:none;
                    border-radius:0 0 16px 16px;
                    padding:24px 48px;
                    text-align:center;
                  ">
                    <div style="
                      font-size:11px;
                      font-weight:800;
                      letter-spacing:4px;
                      color:#333;
                      text-transform:uppercase;
                      margin-bottom:6px;
                    ">STAY FIT &bull; STAY STRONG</div>
                    <div style="font-size:12px;color:#ffc542;font-weight:700;letter-spacing:2px;">
                      AVENUE TEAM
                    </div>
                  </td>
                </tr>

                <!-- ══ BOTTOM ACCENT BAR ══ -->
                <tr>
                  <td style="
                    background:linear-gradient(90deg,#ffc542,#ff5f5f,#ffc542);
                    height:3px;
                    border-radius:0 0 4px 4px;
                  "></td>
                </tr>

              </table>
            </td></tr>
          </table>

        </body>
        </html>
    """,
                name,     // used in header greeting
                service,
                progName,
                priceLine,
                featurePills.toString()
        );

        sendEmail(toEmail, "Avenue Registration Confirmed", htmlMessage);
    }

    public void sendAdminNotification(String adminEmail, JoinBatchRequest request) {

        // Sanitize programType for subject line
        String progNameForSubject = request.getProgramType() != null
                ? request.getProgramType().split("\\n")[0].trim()
                : "Unknown";

        // ── Parse programType (same logic as sendConfirmationEmail) ──────────────
        String[] parts = request.getProgramType() != null
                ? request.getProgramType().split("\\n", -1)
                : new String[]{};

        String progName      = parts.length > 0 ? parts[0].trim() : "—";
        String currentPrice  = parts.length > 1 ? parts[1].trim().replace("/ MONTH", "").trim() : "—";
        String features      = parts.length > 2 ? parts[2].trim() : "—";
        String originalPrice = parts.length > 3 ? parts[3].trim() : null;
        String discount      = parts.length > 4 ? parts[4].trim() : null;

        // ── Discount badge ────────────────────────────────────────────────────────
        String discountBadge = (discount != null && !discount.isEmpty()) ? String.format("""
        <span style="
            display:inline-block;
            background:linear-gradient(135deg,#ff3c3c,#cc0000);
            color:#fff;
            font-size:11px;
            font-weight:800;
            padding:4px 10px;
            border-radius:4px;
            margin-left:10px;
            letter-spacing:1px;
            vertical-align:middle;
            text-transform:uppercase;
            box-shadow:0 2px 8px rgba(255,60,60,0.45);
        ">%s</span>
    """, discount) : "";

        // ── Price line ────────────────────────────────────────────────────────────
        String priceLine;
        if (originalPrice != null && !originalPrice.isEmpty()) {
            priceLine = String.format("""
            <span style="text-decoration:line-through;color:#555;font-size:14px;font-weight:500;margin-right:6px;">%s</span>
            <span style="color:#ffc542;font-size:22px;font-weight:800;letter-spacing:-0.5px;">%s</span>
            <span style="color:#777;font-size:13px;margin-left:4px;">/ MONTH</span>%s
        """, originalPrice, currentPrice, discountBadge);
        } else {
            priceLine = String.format("""
            <span style="color:#ffc542;font-size:22px;font-weight:800;letter-spacing:-0.5px;">%s</span>
            <span style="color:#777;font-size:13px;margin-left:4px;">/ MONTH</span>
        """, currentPrice);
        }

        // ── Feature pills ─────────────────────────────────────────────────────────
        String[] featureList = features.split("\\+");
        StringBuilder featurePills = new StringBuilder();
        for (String f : featureList) {
            featurePills.append(String.format("""
            <span style="
                display:inline-block;
                background:rgba(255,197,66,0.08);
                border:1px solid rgba(255,197,66,0.2);
                color:#aaa;
                font-size:10px;
                font-weight:700;
                letter-spacing:1.2px;
                padding:3px 10px;
                border-radius:20px;
                margin:3px 3px 3px 0;
                text-transform:uppercase;
            ">%s</span>
        """, f.trim()));
        }

        String htmlTemplate = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
      <meta charset="UTF-8"/>
      <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
      <title>New Avenue Registration</title>
    </head>
    <body style="margin:0;padding:0;background:#0a0a0a;font-family:'Segoe UI',Arial,sans-serif;">

      <table width="100%%" cellpadding="0" cellspacing="0" style="background:#0a0a0a;padding:40px 16px;">
        <tr><td align="center">
          <table width="580" cellpadding="0" cellspacing="0" style="max-width:580px;width:100%%;">

            <!-- ══ TOP ACCENT BAR ══ -->
            <tr>
              <td style="
                background:linear-gradient(90deg,#ffc542,#ff5f5f,#ffc542);
                height:4px;
                border-radius:4px 4px 0 0;
              "></td>
            </tr>

            <!-- ══ HEADER ══ -->
            <tr>
              <td style="
                background:linear-gradient(160deg,#181818 0%%,#111 100%%);
                padding:36px 48px 28px;
                text-align:center;
                border-left:1px solid #222;
                border-right:1px solid #222;
              ">
                <div style="margin-bottom:18px;">
                  <span style="
                    font-size:12px;font-weight:800;
                    letter-spacing:6px;color:#ffc542;text-transform:uppercase;
                  ">BOXING AVENUE</span>
                </div>
                <div style="
                  display:inline-block;
                  background:linear-gradient(135deg,#1a2a3a,#0f1a2a);
                  border:2px solid #2a5a8a;border-radius:50%%;
                  width:64px;height:64px;line-height:64px;
                  font-size:28px;margin-bottom:16px;
                  box-shadow:0 0 28px rgba(42,90,138,0.45);
                ">🏋️</div>
                <h1 style="
                  margin:0 0 6px;font-size:22px;font-weight:800;
                  color:#fff;letter-spacing:1px;text-transform:uppercase;
                ">New Registration Alert</h1>
                <p style="margin:0;color:#888;font-size:13px;letter-spacing:0.5px;">
                  A new user has signed up on Boxing Avenue
                </p>
              </td>
            </tr>

            <!-- ══ BODY ══ -->
            <tr>
              <td style="
                background:#141414;padding:32px 48px;
                border-left:1px solid #222;border-right:1px solid #222;
              ">

                <p style="margin:0 0 24px;font-size:14px;color:#aaa;line-height:1.7;">
                  Hello <strong style="color:#fff;">Admin</strong> 👋 &nbsp;
                  Please review the registration details below and follow up with the user.
                </p>

                <!-- ══ DETAILS CARD ══ -->
                <table width="100%%" cellpadding="0" cellspacing="0" style="
                  background:linear-gradient(135deg,#1c1c1c,#181818);
                  border:1px solid #2e2e2e;border-radius:14px;overflow:hidden;
                  margin-bottom:28px;box-shadow:0 8px 40px rgba(0,0,0,0.5);
                ">
                  <tr>
                    <td colspan="2" style="
                      background:linear-gradient(90deg,#ffc54222,#ff5f5f11,transparent);
                      height:2px;
                    "></td>
                  </tr>

                  <!-- 👤 User Details -->
                  <tr>
                    <td colspan="2" style="padding:20px 24px 10px;">
                      <span style="font-size:10px;font-weight:800;letter-spacing:3px;color:#ffc542;text-transform:uppercase;">
                        👤 &nbsp;User Details
                      </span>
                    </td>
                  </tr>

                  <!-- Name -->
                  <tr>
                    <td style="padding:12px 24px;font-size:11px;font-weight:700;letter-spacing:1.5px;color:#555;text-transform:uppercase;width:38%%;border-bottom:1px solid #232323;">Name</td>
                    <td style="padding:12px 24px;font-size:14px;font-weight:600;color:#fff;border-bottom:1px solid #232323;">%s</td>
                  </tr>

                  <!-- Email -->
                  <tr>
                    <td style="padding:12px 24px;font-size:11px;font-weight:700;letter-spacing:1.5px;color:#555;text-transform:uppercase;border-bottom:1px solid #232323;background:#161616;">Email</td>
                    <td style="padding:12px 24px;font-size:14px;font-weight:500;color:#ccc;border-bottom:1px solid #232323;background:#161616;">
                      <a href="mailto:%s" style="color:#ffc542;text-decoration:none;">%s</a>
                    </td>
                  </tr>

                  <!-- Mobile -->
                  <tr>
                    <td style="padding:12px 24px;font-size:11px;font-weight:700;letter-spacing:1.5px;color:#555;text-transform:uppercase;border-bottom:1px solid #232323;">Mobile</td>
                    <td style="padding:12px 24px;font-size:14px;font-weight:500;color:#ccc;border-bottom:1px solid #232323;">
                      <a href="tel:%s" style="color:#ffc542;text-decoration:none;">%s</a>
                    </td>
                  </tr>

                  <!-- 🥊 Program Details -->
                  <tr>
                    <td colspan="2" style="padding:20px 24px 10px;">
                      <span style="font-size:10px;font-weight:800;letter-spacing:3px;color:#ffc542;text-transform:uppercase;">
                        🥊 &nbsp;Program Details
                      </span>
                    </td>
                  </tr>

                  <!-- Program Type - Rich Card -->
                  <tr>
                    <td style="
                      padding:12px 24px;font-size:11px;font-weight:700;
                      letter-spacing:1.5px;color:#555;text-transform:uppercase;
                      border-bottom:1px solid #232323;background:#161616;
                      vertical-align:top;
                    ">Program</td>
                    <td style="padding:12px 24px;border-bottom:1px solid #232323;background:#161616;">
                      <table width="100%%" cellpadding="0" cellspacing="0" style="
                        background:linear-gradient(135deg,#1c1c1c,#181818);
                        border:1px solid #2e2e2e;border-radius:14px;overflow:hidden;
                        box-shadow:0 8px 40px rgba(0,0,0,0.5);
                      ">
                        <tr>
                          <td style="
                            background:linear-gradient(90deg,#ffc54222,#ff5f5f11,transparent);
                            height:2px;
                          "></td>
                        </tr>
                        <tr>
                          <td style="padding:24px 28px;">
                            <!-- Program name -->
                            <div style="
                              font-size:16px;font-weight:800;color:#fff;
                              letter-spacing:0.3px;margin-bottom:12px;
                            ">%s</div>
                            <!-- Price row -->
                            <div style="margin-bottom:16px;line-height:1;">%s</div>
                            <!-- Divider -->
                            <div style="
                              height:1px;
                              background:linear-gradient(90deg,#2a2a2a,transparent);
                              margin-bottom:14px;
                            "></div>
                            <!-- Feature pills -->
                            <div>%s</div>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <!-- Service -->
                  <tr>
                    <td style="padding:12px 24px;font-size:11px;font-weight:700;letter-spacing:1.5px;color:#555;text-transform:uppercase;border-bottom:1px solid #232323;">Service</td>
                    <td style="padding:12px 24px;font-size:14px;font-weight:600;color:#fff;border-bottom:1px solid #232323;">%s</td>
                  </tr>

                  <!-- Batch Time -->
                  <tr>
                    <td style="padding:12px 24px 20px;font-size:11px;font-weight:700;letter-spacing:1.5px;color:#555;text-transform:uppercase;background:#161616;">Batch Time</td>
                    <td style="padding:12px 24px 20px;background:#161616;">
                      <span style="
                        display:inline-block;
                        background:rgba(255,197,66,0.1);
                        border:1px solid rgba(255,197,66,0.3);
                        color:#ffc542;font-size:13px;font-weight:700;
                        padding:4px 14px;border-radius:20px;letter-spacing:0.5px;
                      ">🕐 %s</span>
                    </td>
                  </tr>

                </table>

                <!-- ══ ACTION NOTE ══ -->
                <table width="100%%" cellpadding="0" cellspacing="0" style="
                  background:#0f1a0f;border:1px solid #1e3a1e;
                  border-radius:10px;margin-bottom:0;
                ">
                  <tr>
                    <td style="padding:16px 20px;">
                      <div style="color:#4caf50;font-size:10px;font-weight:700;letter-spacing:2px;margin-bottom:6px;">
                        ACTION REQUIRED
                      </div>
                      <div style="color:#aaa;font-size:13px;line-height:1.8;">
                        📞 &nbsp;Call the user within <strong style="color:#fff;">24 hours</strong><br/>
                        📋 &nbsp;Confirm their <strong style="color:#fff;">batch slot</strong>,
                             <strong style="color:#fff;">service</strong>
                             &amp; <strong style="color:#fff;">program details</strong><br/>
                        ✅ &nbsp;Mark them as onboarded in the system
                      </div>
                    </td>
                  </tr>
                </table>

              </td>
            </tr>

            <!-- ══ FOOTER ══ -->
            <tr>
              <td style="
                background:#0f0f0f;border:1px solid #1a1a1a;border-top:none;
                border-radius:0 0 16px 16px;padding:20px 48px;text-align:center;
              ">
                <div style="font-size:11px;font-weight:700;letter-spacing:3px;color:#333;text-transform:uppercase;margin-bottom:4px;">
                  AUTOMATED NOTIFICATION
                </div>
                <div style="font-size:11px;color:#444;">
                  Avenue Registration System &bull; &copy; 2026 Avenue Team
                </div>
              </td>
            </tr>

            <!-- ══ BOTTOM ACCENT BAR ══ -->
            <tr>
              <td style="
                background:linear-gradient(90deg,#ffc542,#ff5f5f,#ffc542);
                height:3px;border-radius:0 0 4px 4px;
              "></td>
            </tr>

          </table>
        </td></tr>
      </table>

    </body>
    </html>
    """;

        String htmlContent = String.format(
                htmlTemplate,
                request.getName(),          // %s — Name
                request.getEmail(),         // %s — Email href
                request.getEmail(),         // %s — Email display
                request.getMobile(),        // %s — Mobile href
                request.getMobile(),        // %s — Mobile display
                progName,                   // %s — Program card: name
                priceLine,                  // %s — Program card: price row
                featurePills.toString(),    // %s — Program card: feature pills
                request.getService(),       // %s — Service
                request.getTime()           // %s — Batch Time
        );

        sendEmail(adminEmail, "🏋️ New Registration — " + progNameForSubject, htmlContent);
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
              <b>Avenue Team</b>
            </p>

          </div>

        </body>
        </html>
        """, name, resetLink);

        sendEmail(toEmail, "Password Reset Request", htmlMessage);
    }
}