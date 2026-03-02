package com.project.login.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 10)
    private String mobileNumber;

    @Column(nullable = false, length = 50)
    private String utrNumber;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    /* ========= CONSTRUCTORS ========= */
    public Payment() {
    }

    public Payment(String email, String mobileNumber, String utrNumber, LocalDateTime paymentDate) {
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.utrNumber = utrNumber;
        this.paymentDate = paymentDate;
    }

    /* ========= GETTERS & SETTERS ========= */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUtrNumber() {
        return utrNumber;
    }

    public void setUtrNumber(String utrNumber) {
        this.utrNumber = utrNumber;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", utrNumber='" + utrNumber + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
