package com.smaragda_prasianaki.accountmanagement.controller;

import com.smaragda_prasianaki.accountmanagement.dto.BalanceDTO;
import com.smaragda_prasianaki.accountmanagement.dto.MaxWithdrawDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.service.AccountManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class AccountManagementController {
    private final AccountManagementService accountManagementService;

    public AccountManagementController(AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }

    @GetMapping("/{beneficiaryId}")
    public Beneficiary getBeneficiary(@PathVariable String beneficiaryId) {
        return accountManagementService.getBeneficiaryById(beneficiaryId);
    }

    @GetMapping("/{beneficiaryId}/accounts")
    public List<Account> getAccountsByBeneficiary(@PathVariable String beneficiaryId) {
        return accountManagementService.getAccountsByBeneficiaryId(beneficiaryId);
    }

    @GetMapping("/{beneficiaryId}/transactions")
    public List<Transaction> getTransactionsByBeneficiary(@PathVariable String beneficiaryId) {
        return accountManagementService.getTransactionsByBeneficiaryId(beneficiaryId);
    }

    @GetMapping("/{beneficiaryId}/balance")
    public BalanceDTO getBalancesByBeneficiaryId(@PathVariable String beneficiaryId) {
        return accountManagementService.getBalancesByBeneficiaryId(beneficiaryId);
    }

    @GetMapping("/{beneficiaryId}/transactions/maxWithdrawLastMonth")
    public MaxWithdrawDTO getMaxWithdrawalLastMonth(@PathVariable String beneficiaryId) {
        return accountManagementService.getMaxWithdrawalLastMonth(beneficiaryId);
    }
}
