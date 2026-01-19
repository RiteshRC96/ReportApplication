package com.project.login.controller;

import com.project.login.entity.User;
import com.project.login.service.UserService;
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

    @GetMapping("/")
    public String loginPage() {
    	System.out.println("Page Visited: " + "Login");
        return "login";
    }

    // Avoid direct GET /login
    @GetMapping("/login")
    public String loginRedirect() {
    	System.out.println("Page Visited: " + "Login");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
    	System.out.println("Page Visited: " + "Register");
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
    	System.out.println("Page Visited: " + "Dashboard");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Safety check (important)
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return "redirect:/"; // back to login
        }

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String username = userDetails.getName();

        System.out.println("UserName is : " + username);

        model.addAttribute("username", username);

        return "dashboard";
    }


}
