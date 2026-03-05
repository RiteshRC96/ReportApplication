package com.project.login.service;

import com.project.login.entity.Admin;
import com.project.login.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get admin by username
     */
    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    /**
     * Validate admin credentials
     */
    public boolean validateAdminCredentials(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            return passwordEncoder.matches(password, admin.getPassword());
        }
        return false;
    }

    /**
     * Initialize default admin if not exists
     */
    public void initializeDefaultAdmin() {
        Admin admin = adminRepository.findByUsername("admin");
        if (admin == null) {
            Admin newAdmin = new Admin();
            newAdmin.setUsername("admin");
            // Password: admin@123 (hashed)
            newAdmin.setPassword(passwordEncoder.encode("admin@123"));
            newAdmin.setName("Administrator");
            adminRepository.save(newAdmin);
        }
    }
}
