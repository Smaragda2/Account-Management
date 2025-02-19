package com.smaragda_prasianaki.accountmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String transactionId;
    private String accountId;
    private double amount;
    private TransactionType type;  // "deposit" or "withdrawal"
    private String date;
}
