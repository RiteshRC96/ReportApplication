package com.project.login.controller;

import com.project.login.service.OtpService;
import com.project.login.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!userService.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email ID is not registered.");
            return "redirect:/forgot-password";
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/forgot-password";
        }

        // Store data in session
        session.setAttribute("resetEmail", email);
        session.setAttribute("resetPassword", password);

        // Send OTP
        try {
            otpService.sendOtp(email);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }

        redirectAttributes.addFlashAttribute("message", "OTP has been sent to your email.");
        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(HttpSession session) {
        if (session.getAttribute("resetEmail") == null) {
            return "redirect:/forgot-password";
        }
        return "verify_otp";
    }

    @PostMapping("/verify-otp")
    public String handleVerifyOtp(
            @RequestParam("otp") String otp,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String email = (String) session.getAttribute("resetEmail");
        String newPassword = (String) session.getAttribute("resetPassword");

        if (email == null || newPassword == null) {
            return "redirect:/forgot-password";
        }

        String status = otpService.verifyOtp(email, otp);

        if ("VALID".equals(status)) {
            userService.updatePassword(email, newPassword);
            session.removeAttribute("resetEmail");
            session.removeAttribute("resetPassword");
            redirectAttributes.addFlashAttribute("message", "Password updated successfully. Please login.");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", status);
            return "redirect:/verify-otp";
        }
    }
}
