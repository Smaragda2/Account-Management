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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    private ConcurrentMap<String, Transaction> transactionsMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadData() {
        transactionsMap = transactionRepository.loadData()
                .stream()
                .collect(Collectors.toConcurrentMap(Transaction::getTransactionId, Function.identity()));
    }

    public List<Transaction> getTransactionsByAccountId(String accountId) {
        return transactionsMap.values().stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .toList();
    }

    public List<Transaction> getTransactionsByAccountIds(List<String> accountIds) {
        return transactionsMap.values().stream()
                .filter(transaction -> accountIds.contains(transaction.getAccountId()))
                .toList();
    }

    public double getMaxWithdrawalLastMonth(List<String> accountIds) {
        return transactionsMap.values().stream()
                .filter(transaction -> accountIds.contains(transaction.getAccountId()))
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
