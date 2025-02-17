package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.AccountBalanceDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AccountManagementServiceTest {
    @InjectMocks
    private AccountService accountService;
    @InjectMocks
    private TransactionService transactionService;
    @InjectMocks
    private AccountManagementService accountManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalancesByBeneficiaryId() {
        // Arrange
        String beneficiaryId = "1";
        Account account1 = new Account("1", beneficiaryId);
        Account account2 = new Account("2", beneficiaryId);

        when(accountService.getAccountsByBeneficiaryId(beneficiaryId))
                .thenReturn(List.of(account1, account2));

        when(transactionService.getTransactionsByAccountId("1"))
                .thenReturn(List.of(
                        new Transaction("1", "1", 100.0, TransactionType.DEPOSIT, "10/01/25"),
                        new Transaction("2", "1", 50.0, TransactionType.WITHDRAWAL, "10/02/25")
                ));

        when(transactionService.getTransactionsByAccountId("2"))
                .thenReturn(List.of(
                        new Transaction("3", "2", 200.0, TransactionType.DEPOSIT, "10/03/25"),
                        new Transaction("4", "2", 75.0, TransactionType.WITHDRAWAL, "10/04/25")
                ));

        // Act
        List<AccountBalanceDTO> balances = accountManagementService.getBalancesByBeneficiaryId(beneficiaryId);

        // Assert
        assertThat(balances).hasSize(2);
        assertThat(balances.get(0).getAccountId()).isEqualTo("1");
        assertThat(balances.get(0).getBalance()).isEqualTo(50.0); // 100 - 50
        assertThat(balances.get(1).getAccountId()).isEqualTo("2");
        assertThat(balances.get(1).getBalance()).isEqualTo(125.0); // 200 - 75
    }
}
