package com.project.security;

import com.project.login.entity.User;
import com.project.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

    	User user = userRepository.findByEmail(email.trim().toLowerCase()).orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );

    	return new CustomUserDetails(user);

    }
}
