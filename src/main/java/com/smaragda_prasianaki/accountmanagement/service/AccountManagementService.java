package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.dto.BalanceDTO;
import com.smaragda_prasianaki.accountmanagement.dto.MaxWithdrawDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountManagementService {
    private final BeneficiaryService beneficiaryService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountManagementService(BeneficiaryService beneficiaryService, AccountService accountService, TransactionService transactionService) {
        this.beneficiaryService = beneficiaryService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public Beneficiary getBeneficiaryById(String beneficiaryId) {
        return beneficiaryService.getBeneficiaryById(beneficiaryId);
    }

    public List<Account> getAccountsByBeneficiaryId(String beneficiaryId) {
        return accountService.getAccountsByBeneficiaryId(beneficiaryId);
    }

    public List<Transaction> getTransactionsByBeneficiaryId(String beneficiaryId) {
        return transactionService.getTransactionsByAccountIds(getAccountIdsByBeneficiaryId(beneficiaryId));
    }

    public BalanceDTO getBalancesByBeneficiaryId(String beneficiaryId) {
        return accountService.getBalancesByBeneficiaryId(beneficiaryId);
    }

    public MaxWithdrawDTO getMaxWithdrawalLastMonth(String beneficiaryId) {
        return transactionService.getMaxWithdrawalLastMonth(getAccountIdsByBeneficiaryId(beneficiaryId), beneficiaryId);
    }

    private List<String> getAccountIdsByBeneficiaryId(String beneficiaryId) {
        return accountService.getAccountsByBeneficiaryId(beneficiaryId).stream()
                .map(Account::getAccountId)
                .toList();
    }
}
