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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

@Controller
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private com.project.login.service.WalletService walletService;

    @Autowired
    private com.project.login.service.WeaverTraderService weaverTraderService;

    @Autowired
    private com.project.login.service.QualityMasterService qualityMasterService;

    @Autowired
    private com.project.login.service.JobContractService jobContractService;

    @GetMapping("/")
    public String loginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
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

    /**
     * Handle inactive account redirect from Spring Security
     */
    @GetMapping("/account-inactive")
    public String accountInactive(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.trim().isEmpty()) {
            PaymentStatus realStatus = paymentService.getPaymentStatus(email);
            if (realStatus != PaymentStatus.APPROVED) {
                model.addAttribute("status", realStatus);
                if (realStatus == PaymentStatus.REJECTED) {
                    var payment = paymentService.getLatestPaymentByEmail(email);
                    if (payment != null) {
                        model.addAttribute("rejectionReason", payment.getAdminNotes());
                    }
                }
            } else {
                // If payment is approved but they are inactive, it means admin manually disabled them
                model.addAttribute("status", PaymentStatus.INACTIVE_ACCOUNT);
            }
            model.addAttribute("userEmail", email);
        } else {
            model.addAttribute("status", PaymentStatus.INACTIVE_ACCOUNT);
        }
        return "subscription_invalid";
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

        // 🔥 Check if user is inactive
        User user = service.findByEmail(email).orElse(null);
        if (user != null && !user.isActive()) {
            model.addAttribute("status", PaymentStatus.INACTIVE_ACCOUNT);
            model.addAttribute("userEmail", email);
            return "subscription_invalid";
        }

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

        // Update last active date on successful dashboard access
        if (user != null) {
            user.setLastActiveDate(java.time.LocalDateTime.now());
            service.saveUserEntity(user);
        }

        model.addAttribute("username", username);
        model.addAttribute("walletBalance", walletService.getBalance(userDetails.getId()));
        model.addAttribute("weaverCount", weaverTraderService.countWeavers(userDetails.getId()));
        model.addAttribute("traderCount", weaverTraderService.countTraders(userDetails.getId()));
        model.addAttribute("qualityCount", qualityMasterService.countQualities(userDetails.getId()));
        model.addAttribute("contractCount", jobContractService.countContracts(userDetails.getId()));
        model.addAttribute("recentContracts", jobContractService.getRecentContracts(userDetails.getId()));
        model.addAttribute("recentTransactions", walletService.getRecentTransactions(userDetails.getId()));
        return "dashboard";
    }
}