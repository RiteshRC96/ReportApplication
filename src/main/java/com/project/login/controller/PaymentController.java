package com.project.login.controller;

import com.project.login.entity.Payment;
import com.project.login.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Display payment page with QR code
     */
    @GetMapping
    public String showPaymentPage(Model model) {
        return "payment";
    }

    /**
     * Process payment submission
     */
    @PostMapping
    public String processPayment(
            @RequestParam("email") String email,
            @RequestParam("mobileNumber") String mobileNumber,
            @RequestParam("utrNumber") String utrNumber,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Validate inputs
            if (email == null || email.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email is required");
                return "redirect:/payment";
            }

            if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Mobile number is required");
                return "redirect:/payment";
            }
            
            if (utrNumber == null || utrNumber.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "UTR number is required");
                return "redirect:/payment";
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                redirectAttributes.addFlashAttribute("error", "Invalid email format");
                return "redirect:/payment";
            }

            // Validate mobile number (10 digits)
            if (!mobileNumber.matches("\\d{10}")) {
                redirectAttributes.addFlashAttribute("error", "Mobile number must be 10 digits");
                return "redirect:/payment";
            }

            // Save payment
            Payment payment = paymentService.savePayment(email, mobileNumber, utrNumber);
            
            redirectAttributes.addFlashAttribute("success", 
                "Payment received! Your subscription is being processed. UTR: " + payment.getUtrNumber());
            
            return "redirect:/payment";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error processing payment: " + e.getMessage());
            return "redirect:/payment";
        }
    }
}
