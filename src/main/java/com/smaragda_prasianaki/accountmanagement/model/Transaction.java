package com.smaragda_prasianaki.accountmanagement.model;

import lombok.Data;

@Data
public class Transaction {
    private String transactionId;
    private String accountId;
    private double amount;
    private TransactionType type;  // "deposit" or "withdrawal"
    private String date;
}
