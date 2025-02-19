package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.dto.BalanceDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import com.smaragda_prasianaki.accountmanagement.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private AccountService accountService;

    String beneficiaryId1 = "1";
    String beneficiaryId2 = "2";

    String accountId1 = "1";
    String accountId2 = "2";
    String accountId3 = "3";
    Account account1 = new Account(accountId1, beneficiaryId1);
    Account account2 = new Account(accountId2, beneficiaryId1);
    Account account3 = new Account(accountId3, beneficiaryId2);

    Transaction transaction1 = new Transaction("1", accountId1, 100.0, TransactionType.DEPOSIT, "01/01/25");
    Transaction transaction2 = new Transaction("2", accountId1, 50.0, TransactionType.WITHDRAWAL, "01/02/25");
    Transaction transaction3 = new Transaction("3", accountId2, 200.0, TransactionType.DEPOSIT, "01/03/25");

    @BeforeEach
    void setUp() {
        when(accountRepository.loadData())
                .thenReturn(List.of(account1, account2, account3));
    }

    @Test
    void testGetAccountsByBeneficiaryId() {
        // Manually call loadData() to populate the map
        accountService.loadData();

        // Act
        List<Account> accounts = accountService.getAccountsByBeneficiaryId(beneficiaryId1);

        // Assert
        assertThat(accounts).hasSize(2);
        assertThat(accounts.get(0).getAccountId()).isEqualTo("1");
        assertThat(accounts.get(1).getAccountId()).isEqualTo("2");
    }

    @Test
    void testGetBalancesByBeneficiaryId() {
        when(transactionService.getTransactionsByAccountId(accountId1))
                .thenReturn(List.of(transaction1, transaction2));
        when(transactionService.getTransactionsByAccountId(accountId2))
                .thenReturn(List.of(transaction3));
        // Manually call loadData() to populate the map
        accountService.loadData();

        // Act
        BalanceDTO balance = accountService.getBalancesByBeneficiaryId(beneficiaryId1);

        // Assert
        assertThat(balance.getAccountBalances()).hasSize(2);
        assertThat(balance.getAccountBalances().get(0).getAccountId()).isEqualTo(accountId1);
        assertThat(balance.getAccountBalances().get(0).getBalance()).isEqualTo(50.0); // 100 - 50
        assertThat(balance.getAccountBalances().get(1).getAccountId()).isEqualTo(accountId2);
        assertThat(balance.getAccountBalances().get(1).getBalance()).isEqualTo(200.0); // 200
        assertThat(balance.getTotalBalance()).isEqualTo(250.0);
    }
}
