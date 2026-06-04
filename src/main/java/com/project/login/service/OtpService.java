package com.project.login.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder encoder;

    // Cache configuration: Max size 10,000, Expiry 5 minutes
    private final Cache<String, OtpDetails> otpCache = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public static class OtpDetails {
        private String otp;
        private long createdTime;
        private int attempts;

        public OtpDetails(String otp, long createdTime) {
            this.otp = otp;
            this.createdTime = createdTime;
            this.attempts = 0;
        }

        public String getOtp() { return otp; }
        public long getCreatedTime() { return createdTime; }
        public int getAttempts() { return attempts; }
        public void incrementAttempts() { this.attempts++; }
    }

    public void sendOtp(String email) {
    	System.out.println("Send OTP method called from service");
        OtpDetails existingDetails = otpCache.getIfPresent(email);
        long currentTime = System.currentTimeMillis();

        if (existingDetails != null) {
            long timeDiffSeconds = (currentTime - existingDetails.getCreatedTime()) / 1000;
            if (timeDiffSeconds < 60) {
                throw new IllegalStateException("Please wait 60 seconds before resending OTP.");
            }
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        
        
        String otpEncoded = encoder.encode(otp);
        OtpDetails newDetails = new OtpDetails(otpEncoded, currentTime);
        otpCache.put(email, newDetails);
        
        emailService.sendOtp(email, otp);
    }

    public String verifyOtp(String email, String otp) {
        OtpDetails details = otpCache.getIfPresent(email);

        if (details == null) {
            return "OTP expired or not found.";
        }

        if (details.getAttempts() >= 3) {
            otpCache.invalidate(email);
            return "Maximum verification attempts reached. Please request a new OTP.";
        }

        if (encoder.matches(otp, details.getOtp())) {
            otpCache.invalidate(email);
            return "VALID";
        } else {
            details.incrementAttempts();
            otpCache.put(email, details); // Update attempts in cache
            return "Invalid OTP. " + (3 - details.getAttempts()) + " attempts remaining.";
        }
    }
}
