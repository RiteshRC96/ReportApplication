package com.project.login.repository;

import com.project.login.entity.Payment;
import com.project.login.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByMobileNumber(String mobileNumber);
    List<Payment> findByEmailIgnoreCaseOrderByPaymentDateDesc(String email);
    List<Payment> findAllByOrderByPaymentDateDesc();
    boolean existsByUtrNumber(String utrNumber);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE LOWER(p.email) = LOWER(:email) ORDER BY p.paymentDate DESC")
    List<Payment> findByEmailIgnoreCase(@Param("email") String email);

    List<Payment> findByUserId(Long userId);

    /**
     * Find all approved payments whose subscription has expired (endDate < today)
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'APPROVED' AND p.subscriptionEndDate < :today")
    List<Payment> findExpiredSubscriptions(@Param("today") LocalDate today);
}
