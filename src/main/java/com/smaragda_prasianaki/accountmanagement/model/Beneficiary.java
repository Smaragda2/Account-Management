package com.smaragda_prasianaki.accountmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beneficiary {
    private String beneficiaryId;
    private String firstName;
    private String lastName;
}
