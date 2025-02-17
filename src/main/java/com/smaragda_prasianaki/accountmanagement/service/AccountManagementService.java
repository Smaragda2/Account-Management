package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountManagementService {
    private final BeneficiaryService beneficiaryService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public Beneficiary getBeneficiaryById(String beneficiaryId) {
        return beneficiaryService.getBeneficiaryById(beneficiaryId);
    }

    public List<Account> getAccountsByBeneficiaryId(String beneficiaryId) {
        return accountService.getAccountsByBeneficiaryId(beneficiaryId);
    }

    public List<Transaction> getTransactionsByBeneficiaryId(String beneficiaryId) {
        return transactionService.getTransactionsByBeneficiaryId(beneficiaryId);
    }

    public double getTotalBalanceByBeneficiaryId(String beneficiaryId) {
        return accountService.getTotalBalanceByBeneficiaryId(beneficiaryId);
    }

    public double getMaxWithdrawalLastMonth(String beneficiaryId) {
        return transactionService.getMaxWithdrawalLastMonth(beneficiaryId);
    }
}
