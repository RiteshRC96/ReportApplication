package com.project.login.service;

import com.project.login.entity.Payment;
import com.project.login.entity.User;
import com.project.login.enums.PaymentStatus;
import com.project.login.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    // Author:-ritesh CHougule

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    /**
     * Save payment details (with user_id linking)
     */
    public Payment savePayment(String email, String userName,
            String mobileNumber, String utrNumber, Double amount) {

        Payment payment = new Payment();
        payment.setEmail(email.trim().toLowerCase());
        payment.setUserName(userName);
        payment.setMobileNumber(mobileNumber);
        payment.setUtrNumber(utrNumber.trim().toUpperCase());
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        // Link user_id to payment
        Optional<User> userOpt = userService.findByEmail(email.trim().toLowerCase());
        userOpt.ifPresent(user -> payment.setUserId(user.getId()));

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    public org.springframework.data.domain.Page<Payment> getPaginatedPayments(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by("paymentDate").descending());
        return paymentRepository.findAll(pageable);
    }

    public List<Payment> getAllPaymentsOrderByPaymentDate() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    public boolean isUtrExists(String utr) {
        return paymentRepository.existsByUtrNumber(utr.trim().toUpperCase());
    }

    /**
     * Get latest payment by email
     */
    public Payment getLatestPaymentByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        List<Payment> payments = paymentRepository.findByEmailIgnoreCaseOrderByPaymentDateDesc(email.trim());

        if (payments == null || payments.isEmpty()) {
            return null;
        }

        return payments.get(0);
    }

    /**
     * Approve payment
     */
    public Payment approvePayment(Long paymentId,
            LocalDate startDate,
            LocalDate endDate) {

        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null)
            return null;

        payment.setStatus(PaymentStatus.APPROVED);
        payment.setSubscriptionStartDate(startDate);
        payment.setSubscriptionEndDate(endDate);
        payment.setApprovalDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // ✅ Add funds to wallet if amount exists
        if (savedPayment.getAmount() != null && savedPayment.getAmount() > 0) {
            userService.findByEmail(savedPayment.getEmail()).ifPresent(user -> {
                walletService.addFunds(user, savedPayment.getAmount(),
                        "Payment approved for UTR: " + savedPayment.getUtrNumber());
            });
        }

        // ✅ Activate user when payment is approved
        userService.findByEmail(savedPayment.getEmail()).ifPresent(user -> {
            if (!user.isActive()) {
                user.setActive(true);
                user.setLastActiveDate(LocalDateTime.now());
                userService.saveUserEntity(user);
            }
        });

        return savedPayment;
    }

    /**
     * Edit subscription period (for already approved payments)
     */
    public Payment editSubscriptionPeriod(Long paymentId, LocalDate startDate, LocalDate endDate) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null)
            return null;

        payment.setSubscriptionStartDate(startDate);
        payment.setSubscriptionEndDate(endDate);

        return paymentRepository.save(payment);
    }

    /**
     * Reject payment
     */
    public Payment rejectPayment(Long paymentId, String notes) {

        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null)
            return null;

        payment.setStatus(PaymentStatus.REJECTED);
        payment.setAdminNotes(notes);
        payment.setApprovalDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Check if subscription is valid
     */
    public boolean hasValidSubscription(String email) {
        return getPaymentStatus(email) == PaymentStatus.APPROVED;
    }

    /**
     * Get payment status safely
     */
    public PaymentStatus getPaymentStatus(String email) {

        Payment payment = getLatestPaymentByEmail(email);

        if (payment == null) {
            return PaymentStatus.NOT_FOUND;
        }

        // If it's already approved, we must still check if the current date is within
        // the valid range
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            LocalDate today = LocalDate.now();

            // Check if subscription has not started yet
            if (payment.getSubscriptionStartDate() != null && today.isBefore(payment.getSubscriptionStartDate())) {
                return PaymentStatus.NOT_STARTED;
            }

            // Check if subscription has expired
            if (payment.getSubscriptionEndDate() != null && today.isAfter(payment.getSubscriptionEndDate())) {
                return PaymentStatus.EXPIRED;
            }
        }

        return payment.getStatus();
    }

    /**
     * Find expired subscriptions
     */
    public List<Payment> findExpiredSubscriptions() {
        return paymentRepository.findExpiredSubscriptions(LocalDate.now());
    }

    /**
     * Delete payment
     */
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    /**
     * Find payment by ID
     */
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
}