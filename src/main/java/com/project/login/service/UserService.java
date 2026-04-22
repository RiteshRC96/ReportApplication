package com.project.login.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.login.entity.User;
import com.project.login.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WalletService walletService;

    public void saveUser(User user) {
    	System.out.println("saveuser method hit");
    	String password = passwordEncoder.encode(user.getPassword());
    	user.setPassword(password);
    	User savedUser = repo.save(user);

        // ✅ Create wallet automatically
        if (savedUser != null && savedUser.getId() != null) {
            walletService.createWallet(savedUser);
        }
    }
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }
    
    public User findById(Long id) {
        return repo.findById(id).orElse(null);
    }
    public void updatePassword(String email, String newPassword) {
        repo.findByEmail(email).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            repo.save(user);
        });
    }
}
