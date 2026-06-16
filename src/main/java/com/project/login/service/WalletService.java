package com.project.login.service;

import com.project.login.entity.User;
import com.project.login.entity.Wallet;
import com.project.login.entity.WalletTransaction;
import com.project.login.repository.WalletRepository;
import com.project.login.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    public void createWallet(User user) {
        if (walletRepository.findByUser(user).isPresent()) {
            return;
        }
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);
    }

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId).orElse(null);
    }

    public Double getBalance(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet != null ? wallet.getBalance() : 0.0;
    }

    @Transactional
    public boolean deduct(User user, double amount, String description) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseGet(() -> {
                    createWallet(user);
                    return walletRepository.findByUser(user).get();
                });

        if (wallet.getBalance() < amount) {
            return false;
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setUser(user);
        transaction.setWallet(wallet);
        transaction.setAmount(-amount);
        transaction.setRemainingBal(wallet.getBalance());
        transaction.setTransactionType("DEDUCTION");
        transaction.setDescription(description);
        walletTransactionRepository.save(transaction);

        return true;
    }

    @Transactional
    public void addFunds(User user, double amount, String description) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseGet(() -> {
                    createWallet(user);
                    return walletRepository.findByUser(user).get();
                });

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setUser(user);
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setRemainingBal(wallet.getBalance());
        transaction.setTransactionType("DEPOSIT");
        transaction.setDescription(description);
        walletTransactionRepository.save(transaction);
    }

    public java.util.List<WalletTransaction> getRecentTransactions(Long userId) {
        return walletTransactionRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
    }
}
