package com.project.login.controller;

import com.project.login.entity.Admin;
import com.project.login.entity.Payment;
import com.project.login.service.AdminService;
import com.project.login.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PaymentService paymentService;

    /**
     * Show admin login page
     */
    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin_login";
    }

    /**
     * Process admin login
     */
    @PostMapping("/login")
    public String processAdminLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (adminService.validateAdminCredentials(username, password)) {
                Admin admin = adminService.getAdminByUsername(username);
                session.setAttribute("adminId", admin.getId());
                session.setAttribute("adminUsername", admin.getUsername());
                session.setAttribute("adminName", admin.getName());
                return "redirect:/admin/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                return "redirect:/admin/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error during login: " + e.getMessage());
            return "redirect:/admin/login";
        }
    }

    /**
     * Show admin dashboard
     */
    @GetMapping("/dashboard")
    public String showAdminDashboard(
            @RequestParam(defaultValue = "0") int page,
            HttpSession session, 
            Model model) {
        // Check if admin is logged in
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        org.springframework.data.domain.Page<Payment> paymentPage = paymentService.getPaginatedPayments(page, 5);
        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());
        model.addAttribute("adminName", session.getAttribute("adminName"));

        // Default 1yr Subscription dates
        LocalDate today = LocalDate.now();
        model.addAttribute("defaultStartDate", today);
        model.addAttribute("defaultEndDate", today.plusYears(1));
        
        return "admin_dashboard";
    }

    /**
     * AJAX Endpoint for pagination
     */
    @GetMapping("/dashboard/page")
    public String getPaymentPage(
            @RequestParam(defaultValue = "0") int page,
            HttpSession session,
            Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        org.springframework.data.domain.Page<Payment> paymentPage = paymentService.getPaginatedPayments(page, 5);
        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());

        // Default dates needed for modals inside the fragment
        LocalDate today = LocalDate.now();
        model.addAttribute("defaultStartDate", today);
        model.addAttribute("defaultEndDate", today.plusYears(1));

        return "admin_dashboard :: #payments-table-container";
    }

    /**
     * Approve payment
     */
    @PostMapping("/approve-payment/{paymentId}")
    public String approvePayment(
            @PathVariable("paymentId") Long paymentId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Check if admin is logged in
            if (session.getAttribute("adminId") == null) {
                return "redirect:/admin/login";
            }

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (end.isBefore(start)) {
                redirectAttributes.addFlashAttribute("error", "End date must be after start date");
                return "redirect:/admin/dashboard";
            }

            Payment payment = paymentService.approvePayment(paymentId, start, end);
            if (payment != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Payment approved successfully for " + payment.getUserName());
            } else {
                redirectAttributes.addFlashAttribute("error", "Payment not found");
            }
            
            return "redirect:/admin/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error approving payment: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    /**
     * Reject payment
     */
    @PostMapping("/reject-payment/{paymentId}")
    public String rejectPayment(
            @PathVariable("paymentId") Long paymentId,
            @RequestParam(value = "notes", required = false) String notes,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Check if admin is logged in
            if (session.getAttribute("adminId") == null) {
                return "redirect:/admin/login";
            }

            Payment payment = paymentService.rejectPayment(paymentId, notes);
            if (payment != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Payment rejected for " + payment.getUserName());
            } else {
                redirectAttributes.addFlashAttribute("error", "Payment not found");
            }
            
            return "redirect:/admin/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error rejecting payment: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    /**
     * Admin logout
     */
    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
