package com.project.login.controller;

import com.project.login.entity.Payment;
import com.project.login.entity.User;
import com.project.login.enums.PaymentStatus;
import com.project.login.service.PaymentService;
import com.project.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Pattern;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("\\d{10}");

    /**
     * Display payment page
     */
    @GetMapping
    public String showPaymentPage(
            @RequestParam(required = false) String email,
            Model model, 
            org.springframework.security.core.Authentication authentication) {
        System.out.println("Visited page: /payment");

        String displayEmail = email;
        String displayName = null;

        if (authentication != null && authentication.getPrincipal() instanceof com.project.security.CustomUserDetails) {
            com.project.security.CustomUserDetails userDetails = (com.project.security.CustomUserDetails) authentication.getPrincipal();
            displayName = userDetails.getName();
            displayEmail = userDetails.getUsername();

            // Check if user is inactive
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null && !user.isActive()) {
                model.addAttribute("status", PaymentStatus.INACTIVE_ACCOUNT);
                model.addAttribute("userEmail", userDetails.getUsername());
                return "subscription_invalid";
            }
        } else if (displayEmail != null && !displayEmail.trim().isEmpty()) {
            User user = userService.findByEmail(displayEmail.trim()).orElse(null);
            if (user != null) {
                displayName = user.getName();
            }
        }

        model.addAttribute("userName", displayName);
        model.addAttribute("email", displayEmail);

        return "payment";
    }

    /**
     * Process payment submission
     */
    @PostMapping
    public String processPayment(
            @RequestParam String email,
            @RequestParam String userName,
            @RequestParam String mobileNumber,
            @RequestParam String utrNumber,
            @RequestParam Double amount,
            org.springframework.security.core.Authentication authentication,
            RedirectAttributes redirectAttributes) {
        System.out.println("Button clicked: Process Payment (/payment POST)");

        try {
            // If user is logged in, force use their session details
            if (authentication != null && authentication.getPrincipal() instanceof com.project.security.CustomUserDetails) {
                com.project.security.CustomUserDetails userDetails = (com.project.security.CustomUserDetails) authentication.getPrincipal();
                email = userDetails.getUsername();
                userName = userDetails.getName();
            }

            // -------- Trim Inputs --------
            email = email != null ? email.trim().toLowerCase() : "";
            userName = userName != null ? userName.trim() : "";
            mobileNumber = mobileNumber != null ? mobileNumber.trim() : "";
            utrNumber = utrNumber != null ? utrNumber.trim().toUpperCase() : "";

            // -------- Basic Validation --------
            if (email.isEmpty())
                return errorRedirect("Email is required", redirectAttributes);

            if (userName.isEmpty())
                return errorRedirect("Name is required", redirectAttributes);

            if (mobileNumber.isEmpty())
                return errorRedirect("Mobile number is required", redirectAttributes);

            if (utrNumber.isEmpty())
                return errorRedirect("UTR number is required", redirectAttributes);

            if (amount == null || amount <= 0)
                return errorRedirect("Amount must be greater than 0", redirectAttributes);

            // -------- Format Validation --------
            if (!EMAIL_PATTERN.matcher(email).matches())
                return errorRedirect("Invalid email format", redirectAttributes);

            if (!MOBILE_PATTERN.matcher(mobileNumber).matches())
                return errorRedirect("Mobile number must be exactly 10 digits", redirectAttributes);

            // -------- Duplicate UTR Check --------
            if (paymentService.isUtrExists(utrNumber))
                return errorRedirect("This UTR number has already been submitted", redirectAttributes);

            // -------- Save Payment --------
            Payment payment = paymentService.savePayment(
                    email, userName, mobileNumber, utrNumber, amount
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Payment received! Your subscription is under review. UTR: "
                            + payment.getUtrNumber()
            );
            System.out.println("Received payment");
            return "redirect:/payment";

        } catch (Exception e) {
            e.printStackTrace();
            return errorRedirect("Something went wrong. Please try again later.", redirectAttributes);
        }
    }

    /**
     * Check Subscription Status (CLEAN VERSION)
     */
    @GetMapping("/status")
    public String checkSubscriptionStatus(@RequestParam String email, Model model) {
        System.out.println("Endpoint hit: Check Subscription Status (/payment/status)");

        email = email != null ? email.trim().toLowerCase() : "";

        // Check if user is inactive
        User user = userService.findByEmail(email).orElse(null);
        if (user != null && !user.isActive()) {
            model.addAttribute("status", PaymentStatus.INACTIVE_ACCOUNT);
            model.addAttribute("userEmail", email);
            return "subscription_invalid";
        }

        PaymentStatus status = paymentService.getPaymentStatus(email);

        model.addAttribute("status", status);
        model.addAttribute("userEmail", email);

        // If rejected → fetch rejection reason
        if (status == PaymentStatus.REJECTED) {
            Payment payment = paymentService.getLatestPaymentByEmail(email);
            if (payment != null) {
                model.addAttribute("rejectionReason", payment.getAdminNotes());
            }
        }

        return "subscription_invalid";
    }

    /**
     * Helper method
     */
    private String errorRedirect(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", message);
        return "redirect:/payment";
    }
}