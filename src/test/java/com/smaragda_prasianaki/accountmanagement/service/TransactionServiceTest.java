package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import com.smaragda_prasianaki.accountmanagement.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        when(transactionRepository.loadData())
                .thenReturn(List.of(transaction1, transaction2, transaction3, transaction4));

    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    String accountId1 = "1";
    String accountId2 = "2";
    String accountId3 = "3";
    Transaction transaction1 = new Transaction("1", accountId1, 100.0, TransactionType.DEPOSIT, "01/01/25");
    Transaction transaction2 = new Transaction("2", accountId1, 50.0, TransactionType.WITHDRAWAL, "01/02/25");
    Transaction transaction3 = new Transaction("3", accountId2, 200.0, TransactionType.DEPOSIT, "01/03/25");
    Transaction transaction4 = new Transaction("4", accountId3, 300.0, TransactionType.WITHDRAWAL, "02/03/25");

    @Test
    void testGetTransactionsByAccountId() {
        // Manually call loadData() after setting up the mock
        transactionService.loadData();

        // Act
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId1);

        // Assert
        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getTransactionId()).isEqualTo("1");
        assertThat(transactions.get(1).getTransactionId()).isEqualTo("2");
    }

    @Test
    void testGetTransactionsByAccountIds() {
        // Manually call loadData() after setting up the mock
        transactionService.loadData();

        // Act
        List<Transaction> transactions = transactionService.getTransactionsByAccountIds(List.of(accountId1, accountId2));

        // Assert
        assertThat(transactions).hasSize(3);
        assertThat(transactions.get(0).getTransactionId()).isEqualTo("1");
        assertThat(transactions.get(1).getTransactionId()).isEqualTo("2");
        assertThat(transactions.get(2).getTransactionId()).isEqualTo("3");
    }

    @Test
    void testGetMaxWithdrawalLastMonth() {
        String lastMonthDate = LocalDate.now().minusMonths(1).format(DATE_FORMATTER);
        String olderDate = LocalDate.now().minusMonths(2).format(DATE_FORMATTER);

        transaction2.setDate(olderDate);
        transaction4.setDate(lastMonthDate);

        // Manually call loadData() after setting up the mock
        transactionService.loadData();

        // Act
        double maxWithdrawal = transactionService.getMaxWithdrawalLastMonth(List.of(accountId1, accountId2, accountId3));

        // Assert
        assertThat(maxWithdrawal).isEqualTo(300.0);
    }

    @Test
    void testGetMaxWithdrawalLastMonth_NoWithdrawalsInLastMonth() {
        String olderDate = LocalDate.now().minusMonths(2).format(DATE_FORMATTER);

        transaction2.setDate(olderDate);
        transaction4.setDate(olderDate);

        // Manually call loadData() after setting up the mock
        transactionService.loadData();

        // Act
        double maxWithdrawal = transactionService.getMaxWithdrawalLastMonth(List.of(accountId1, accountId2, accountId3));

        // Assert
        assertThat(maxWithdrawal).isEqualTo(0.0);
    }

    @Test
    void testGetTransactionsByAccountId_NoTransactions() {
        when(transactionRepository.loadData()).thenReturn(Collections.emptyList());
        transactionService.loadData();

        // Act
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId1);

        // Assert
        assertThat(transactions).isEmpty();
    }
}
