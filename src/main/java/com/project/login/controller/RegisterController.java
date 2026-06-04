package com.project.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.login.entity.User;
import com.project.login.service.OtpService;
import com.project.login.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RegisterController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;   // ✅ Use service instead of repository

    /* ===============================
       STEP 1: Send OTP
    =============================== */
    @PostMapping("/send-register-otp")
    public String sendOtp(HttpSession session,
                          @RequestParam String name,
                          @RequestParam String email,
                          @RequestParam String password,
                          Model model) {

        System.out.println("SEND OTP CONTROLLER HIT");

        // Store user data temporarily in session
        session.setAttribute("REG_NAME", name);
        session.setAttribute("REG_EMAIL", email);
        session.setAttribute("REG_PASSWORD", password);

        // ✅ CHECK IF EMAIL ALREADY EXISTS BEFORE SENDING OTP
        if (userService.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already exists. Please login.");
            return "register";
        }

        try {
            otpService.sendOtp(email);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        model.addAttribute("otpSent", true);
        return "register"; // Stay on register page
    }

    /* ===============================
       STEP 1.5: Resend OTP
    =============================== */
    @PostMapping("/resend-register-otp")
    public String resendOtp(HttpSession session, Model model) {
        String email = (String) session.getAttribute("REG_EMAIL");
        if (email != null) {
            try {
                otpService.sendOtp(email);
            } catch (IllegalStateException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("otpSent", true);
        return "register";
    }

    /* ===============================
       STEP 2: Verify OTP & Save User
    =============================== */
    @PostMapping("/verify-register-otp")
    public String verifyOtp(@RequestParam String otp,
                            HttpSession session,
                            Model model) {

        String email = (String) session.getAttribute("REG_EMAIL");

        if (email == null) {
            return "redirect:/register";
        }

        String status = otpService.verifyOtp(email, otp);

        if (!"VALID".equals(status)) {
            model.addAttribute("otpSent", true);
            model.addAttribute("error", status);
            return "register";
        }


        // Create user
        User user = new User();
        user.setName((String) session.getAttribute("REG_NAME"));
        user.setEmail(email);
        user.setPassword((String) session.getAttribute("REG_PASSWORD"));

        userService.saveUser(user);

        session.invalidate();

        return "redirect:/?regSuccess=true";
    }

}
