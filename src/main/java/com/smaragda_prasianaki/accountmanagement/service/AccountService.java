package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.AccountBalanceDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import com.smaragda_prasianaki.accountmanagement.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    private ConcurrentMap<String, Account> accountsMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadData() {
        accountsMap = accountRepository.loadData()
                .stream()
                .collect(Collectors.toConcurrentMap(Account::getAccountId, Function.identity()));
    }

    public List<Account> getAccountsByBeneficiaryId(String beneficiaryId) {
        return accountsMap.values().stream()
                .filter(account -> account.getBeneficiaryId().equals(beneficiaryId))
                .toList();
    }

    public List<AccountBalanceDTO> getBalancesByBeneficiaryId(String beneficiaryId) {
        return getAccountsByBeneficiaryId(beneficiaryId).stream()
                .map(account -> new AccountBalanceDTO(account.getAccountId(), calculateBalanceForAccount(account.getAccountId())))
                .toList();
    }

    private double calculateBalanceForAccount(String accountId) {
        return transactionService.getTransactionsByAccountId(accountId).stream()
                .mapToDouble(transaction -> transaction.getType() == TransactionType.DEPOSIT ? transaction.getAmount() : -transaction.getAmount())
                .sum();
    }
}
