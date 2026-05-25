package com.project.login.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        user.setActive(true);
        user.setLastActiveDate(LocalDateTime.now());
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

    /**
     * Get paginated list of all users
     */
    public Page<User> getPaginatedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAllByOrderByIdDesc(pageable);
    }

    /**
     * Activate a user
     */
    public User activateUser(Long userId) {
        User user = repo.findById(userId).orElse(null);
        if (user != null) {
            user.setActive(true);
            user.setLastActiveDate(LocalDateTime.now());
            return repo.save(user);
        }
        return null;
    }

    /**
     * Deactivate a user
     */
    public User deactivateUser(Long userId) {
        User user = repo.findById(userId).orElse(null);
        if (user != null) {
            user.setActive(false);
            return repo.save(user);
        }
        return null;
    }

    /**
     * Save/update user directly
     */
    public User saveUserEntity(User user) {
        return repo.save(user);
    }

    /**
     * Get all users (for scheduler)
     */
    public java.util.List<User> findAllUsers() {
        return repo.findAll();
    }
}
