package com.project.login.config;

import com.project.login.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default admin account
        adminService.initializeDefaultAdmin();
        System.out.println("✓ Admin account initialized!");
    }
}
