package com.project.login.repository;

import com.project.login.entity.User;
import com.project.login.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findByUser(User user);
    List<WalletTransaction> findByUserId(Long userId);
    List<WalletTransaction> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);
}
