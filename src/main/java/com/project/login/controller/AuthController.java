package com.project.login.controller;

import com.project.login.entity.User;
import com.project.login.enums.PaymentStatus;
import com.project.login.service.UserService;
import com.project.login.service.PaymentService;
import com.project.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        service.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
            !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return "redirect:/";
        }

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String username = userDetails.getName();
        String email = userDetails.getUsername().trim().toLowerCase();

        // 🔥 Check subscription status using enum
        PaymentStatus status = paymentService.getPaymentStatus(email);

        if (status != PaymentStatus.APPROVED) {

            model.addAttribute("status", status);
            model.addAttribute("userEmail", email);

            // If rejected → add reason
            if (status == PaymentStatus.REJECTED) {
                var payment = paymentService.getLatestPaymentByEmail(email);
                if (payment != null) {
                    model.addAttribute("rejectionReason",
                            payment.getAdminNotes());
                }
            }

            return "subscription_invalid";
        }

        model.addAttribute("username", username);
        return "dashboard";
    }
}