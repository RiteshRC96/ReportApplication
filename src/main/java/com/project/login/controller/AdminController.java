package com.project.login.controller;

import com.project.login.entity.Admin;
import com.project.login.entity.Payment;
import com.project.login.entity.User;
import com.project.login.service.AdminService;
import com.project.login.service.EmailService;
import com.project.login.service.PaymentService;
import com.project.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    
    @GetMapping("/login")
    public String showAdminLogin() {
        System.out.println("Page Visited: Admin login");
        return "admin_login";
    }

    
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
                System.out.println("Admin login successfull ");
                System.out.println("Page Visited: Admin dashboard");
                return "redirect:/admin/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                System.out.println("Invalid username or password");
                return "redirect:/admin/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error during login: " + e.getMessage());
            return "redirect:/admin/login";
        }
    }

   
    @GetMapping("/dashboard")
    public String showAdminDashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "userPage", defaultValue = "0") int userPage,
            HttpSession session, 
            Model model) {
        // Check if admin is logged in
        if (session.getAttribute("adminId") == null) {
        	System.out.println("Secstion logout");
            return "redirect:/admin/login";
        }

        // Payment pagination
        org.springframework.data.domain.Page<Payment> paymentPage = paymentService.getPaginatedPayments(page, 5);
        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());

        // User pagination
        Page<User> usersPage = userService.getPaginatedUsers(userPage, 5);
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentUserPage", userPage);
        model.addAttribute("totalUserPages", usersPage.getTotalPages());

        model.addAttribute("adminName", session.getAttribute("adminName"));

        // Default 1yr Subscription dates
        LocalDate today = LocalDate.now();
        model.addAttribute("defaultStartDate", today);
        model.addAttribute("defaultEndDate", today.plusYears(1));
        
        return "admin_dashboard";
    }

    /**
     * AJAX Endpoint for payment pagination
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
     * AJAX Endpoint for user pagination
     */
    @GetMapping("/dashboard/users-page")
    public String getUserPage(
            @RequestParam(defaultValue = "0") int page,
            HttpSession session,
            Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        Page<User> usersPage = userService.getPaginatedUsers(page, 5);
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentUserPage", page);
        model.addAttribute("totalUserPages", usersPage.getTotalPages());

        return "admin_dashboard :: #users-table-container";
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
     * Edit subscription period (for already approved payments)
     */
    @PostMapping("/edit-subscription/{paymentId}")
    public String editSubscription(
            @PathVariable("paymentId") Long paymentId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (session.getAttribute("adminId") == null) {
                return "redirect:/admin/login";
            }

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (end.isBefore(start)) {
                redirectAttributes.addFlashAttribute("error", "End date must be after start date");
                return "redirect:/admin/dashboard";
            }

            Payment payment = paymentService.editSubscriptionPeriod(paymentId, start, end);
            if (payment != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Subscription period updated for " + payment.getUserName());
            } else {
                redirectAttributes.addFlashAttribute("error", "Payment not found");
            }
            
            return "redirect:/admin/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error editing subscription: " + e.getMessage());
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
     * Toggle user active/inactive
     */
    @PostMapping("/toggle-user-status/{userId}")
    public String toggleUserStatus(
            @PathVariable("userId") Long userId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (session.getAttribute("adminId") == null) {
                return "redirect:/admin/login";
            }

            User user = userService.findById(userId);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/admin/dashboard";
            }

            if (user.isActive()) {
                // Deactivate user
                userService.deactivateUser(userId);
                
                // Send deactivation email
                try {
                    emailService.sendAccountDeactivationEmail(user.getEmail(), user.getName());
                } catch (Exception emailEx) {
                    System.err.println("Failed to send deactivation email: " + emailEx.getMessage());
                }
                
                redirectAttributes.addFlashAttribute("success", 
                    "User '" + user.getName() + "' has been deactivated. Notification email sent.");
            } else {
                // Activate user
                userService.activateUser(userId);
                redirectAttributes.addFlashAttribute("success", 
                    "User '" + user.getName() + "' has been activated.");
            }
            
            return "redirect:/admin/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error toggling user status: " + e.getMessage());
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
