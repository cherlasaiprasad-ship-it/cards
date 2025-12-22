package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    private String merchantName;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        SUCCESS, FAILED, SUSPICIOUS
    }

    // Getters and Setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
