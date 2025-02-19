package com.smaragda_prasianaki.accountmanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class BalanceDTO {
    private List<AccountBalanceDTO> accountBalances;
    private double totalBalance;

    public BalanceDTO(List<AccountBalanceDTO> accountBalances, double totalBalance) {
        this.accountBalances = accountBalances;
        this.totalBalance = totalBalance;
    }
}
