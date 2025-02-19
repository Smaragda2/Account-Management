package com.smaragda_prasianaki.accountmanagement.dto;

import lombok.Data;

@Data
public class MaxWithdrawDTO {
    private double maxWithdraw;
    private String date;

    public MaxWithdrawDTO(double maxWithdraw, String date) {
        this.maxWithdraw = maxWithdraw;
        this.date = date;
    }
}
