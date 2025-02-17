package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import com.smaragda_prasianaki.accountmanagement.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    private ConcurrentMap<String, Transaction> transactionsMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadData() {
        transactionsMap = transactionRepository.loadData()
                .stream()
                .collect(Collectors.toConcurrentMap(Transaction::getTransactionId, Function.identity()));
    }

    public List<Transaction> getTransactionsByBeneficiaryId(String beneficiaryId) {
        return transactionsMap.values().stream()
                .filter(transaction -> accountService.getAccountsByBeneficiaryId(beneficiaryId).stream()
                        .anyMatch(account -> account.getAccountId().equals(transaction.getAccountId())))
                .toList();
    }

    public List<Transaction> getTransactionsByAccountId(String accountId) {
        return transactionsMap.values().stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .toList();
    }

    public double getMaxWithdrawalLastMonth(String beneficiaryId) {
        return getTransactionsByBeneficiaryId(beneficiaryId).stream()
                .filter(transaction -> transaction.getType() == TransactionType.WITHDRAWAL)
                .filter(transaction -> isLastMonth(transaction.getDate()))
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0);
    }

    private boolean isLastMonth(String date) {
        LocalDate transactionDate = LocalDate.parse(date, DATE_FORMATTER);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the first day of the last month
        LocalDate firstDayOfLastMonth = currentDate.minusMonths(1);

        // Check if the transaction date is within the last month
        return !transactionDate.isBefore(firstDayOfLastMonth);
    }
}
