package com.project.login.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @SuppressWarnings("null")
    public void sendOtp(String toEmail, String otp) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("ShreeGuru Software Solution - OTP Verification");

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; background-color: #f4f6f9; padding: 20px;'>"
                            + "<div style='max-width: 600px; margin: auto; background: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>"

                            + "<div style='background: linear-gradient(90deg,#6a11cb,#2575fc); padding: 30px; text-align: center; color: white;'>"
                            + "<h2 style='margin:0;'>ShreeGuru Software Solution</h2>"
                            + "<p style='margin:5px 0 0;'>Secure OTP Verification</p>"
                            + "</div>"

                            + "<div style='padding: 30px; text-align: center;'>"
                            + "<p style='font-size: 16px;'>Use the below One Time Password (OTP) to continue:</p>"

                            + "<div style='display: inline-block; margin: 20px 0; padding: 15px 30px; "
                            + "font-size: 28px; font-weight: bold; letter-spacing: 5px; "
                            + "background: #f0f4ff; border: 2px dashed #2575fc; "
                            + "border-radius: 8px; color: #2575fc;'>"
                            + otp
                            + "</div>"

                            + "<p style='color: #555;'>This OTP is valid for <strong>5 minutes</strong>.</p>"
                            + "<p style='font-size: 13px; color: #888;'>If you did not request this, please ignore this email.</p>"
                            + "</div>"

                            + "<div style='background:#f4f6f9; padding:15px; text-align:center; font-size:12px; color:#999;'>"
                            + "© 2026 ShreeGuru Software Solution | System Generated Email"
                            + "</div>"

                            + "</div>"
                            + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email");
        }
    }

    /**
     * Send account deactivation notification email
     */
    @Async
    public void sendAccountDeactivationEmail(String toEmail, String userName) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("ShreeGuru Software Solution - Account Deactivated");

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; background-color: #f4f6f9; padding: 20px;'>"
                            + "<div style='max-width: 600px; margin: auto; background: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>"

                            + "<div style='background: linear-gradient(90deg, #e74c3c, #c0392b); padding: 30px; text-align: center; color: white;'>"
                            + "<h2 style='margin:0;'>ShreeGuru Software Solution</h2>"
                            + "<p style='margin:5px 0 0;'>Account Notification</p>"
                            + "</div>"

                            + "<div style='padding: 30px;'>"
                            + "<p style='font-size: 16px; color: #333;'>Dear <strong>" + userName + "</strong>,</p>"

                            + "<p style='font-size: 15px; color: #555; line-height: 1.8;'>"
                            + "Your account has been <strong style='color: #e74c3c;'>inactivated</strong> by the administrator. "
                            + "Please <a href='https://shreegurusoftwaresolution.com/' "
                            + "style='color:#3498db; text-decoration:none;'>contact</a> "
                            + "the admin to activate your account."
                            + "</p>"

                            + "<div style='background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 4px;'>"
                            + "<p style='margin: 0; font-size: 14px; color: #856404;'>"
                            + "<strong>NOTE:</strong> Account will be deleted after inactivity of <strong>180 days</strong>."
                            + "</p>"
                            + "</div>"

                            + "<p style='font-size: 14px; color: #777;'>"
                            + "If you believe this is a mistake, please reach out to the administrator immediately."
                            + "</p>"
                            + "</div>"

                            + "<div style='background:#f4f6f9; padding:15px; text-align:center; font-size:12px; color:#999;'>"
                            + "© 2026 ShreeGuru Software Solution | System Generated Email"
                            + "</div>"

                            + "</div>"
                            + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);

            System.out.println("Account deactivation email sent to: " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send deactivation email to: " + toEmail);
        }
    }
}
