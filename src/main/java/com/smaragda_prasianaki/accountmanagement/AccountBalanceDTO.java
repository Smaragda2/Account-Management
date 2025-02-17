package com.smaragda_prasianaki.accountmanagement;

import lombok.Data;

@Data
public class AccountBalanceDTO {
    private String accountId;
    private double balance;

    public AccountBalanceDTO(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }
}
