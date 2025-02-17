package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import com.smaragda_prasianaki.accountmanagement.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionsByAccountId() {
        // Arrange
        String accountId = "1";
        Transaction transaction1 = new Transaction("1", accountId, 100.0, TransactionType.DEPOSIT, "10/01/23");
        Transaction transaction2 = new Transaction("2", accountId, 50.0, TransactionType.WITHDRAWAL, "10/02/23");

        // Mock the behavior of the repository
        when(transactionRepository.loadData())
                .thenReturn(List.of(transaction1, transaction2));

        // Act
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);

        // Assert
        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getTransactionId()).isEqualTo("1");
        assertThat(transactions.get(1).getTransactionId()).isEqualTo("2");
    }
}
