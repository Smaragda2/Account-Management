package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.AccountBalanceDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountManagementServiceTest {
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private BeneficiaryService beneficiaryService;

    @InjectMocks
    private AccountManagementService accountManagementService;

    String beneficiaryId = "1";
    Beneficiary beneficiary = new Beneficiary(beneficiaryId, "John", "Doe");
    List<String> accountIds = List.of("1", "2");
    Account account1 = new Account("1", beneficiaryId);
    Account account2 = new Account("2", beneficiaryId);
    Transaction transaction1 = new Transaction("1", account1.getAccountId(), 100.0, TransactionType.DEPOSIT, "10/01/25");
    Transaction transaction2 = new Transaction("2", account2.getAccountId(), 200.0, TransactionType.WITHDRAWAL, "10/02/25");
    AccountBalanceDTO balance1 = new AccountBalanceDTO(account1.getAccountId(), 50.0);
    AccountBalanceDTO balance2 = new AccountBalanceDTO(account2.getAccountId(), 125.0);


    @Test
    void testGetBeneficiaryById() {
        when(beneficiaryService.getBeneficiaryById(beneficiaryId)).thenReturn(beneficiary);

        Beneficiary result = accountManagementService.getBeneficiaryById(beneficiaryId);

        // Assert
        assertThat(result).isEqualTo(beneficiary);
        verify(beneficiaryService, times(1)).getBeneficiaryById(beneficiaryId);
    }

    @Test
    void testGetAccountsByBeneficiaryId() {
        when(accountService.getAccountsByBeneficiaryId(beneficiaryId)).thenReturn(List.of(account1, account2));

        List<Account> accounts = accountManagementService.getAccountsByBeneficiaryId(beneficiaryId);

        // Assert
        assertThat(accounts).hasSize(2);
        assertThat(accounts.get(0).getAccountId()).isEqualTo("1");
        assertThat(accounts.get(1).getAccountId()).isEqualTo("2");
        verify(accountService, times(1)).getAccountsByBeneficiaryId(beneficiaryId);
    }

    @Test
    void testGetTransactionsByBeneficiaryId() {
        when(transactionService.getTransactionsByAccountIds(accountIds)).thenReturn(List.of(transaction1, transaction2));
        when(accountService.getAccountsByBeneficiaryId(beneficiaryId)).thenReturn(List.of(account1, account2));

        List<Transaction> transactions = accountManagementService.getTransactionsByBeneficiaryId(beneficiaryId);

        // Assert
        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getTransactionId()).isEqualTo("1");
        assertThat(transactions.get(1).getTransactionId()).isEqualTo("2");
        verify(transactionService, times(1)).getTransactionsByAccountIds(accountIds);
    }

    @Test
    void testGetBalancesByBeneficiaryId() {
        when(accountService.getBalancesByBeneficiaryId(beneficiaryId)).thenReturn(List.of(balance1, balance2));

        List<AccountBalanceDTO> balances = accountManagementService.getBalancesByBeneficiaryId(beneficiaryId);

        // Assert
        assertThat(balances).hasSize(2);
        assertThat(balances.get(0).getAccountId()).isEqualTo("1");
        assertThat(balances.get(0).getBalance()).isEqualTo(50.0);
        assertThat(balances.get(1).getAccountId()).isEqualTo("2");
        assertThat(balances.get(1).getBalance()).isEqualTo(125.0);
        verify(accountService, times(1)).getBalancesByBeneficiaryId(beneficiaryId);
    }

    @Test
    void testGetMaxWithdrawalLastMonth() {
        when(transactionService.getMaxWithdrawalLastMonth(accountIds)).thenReturn(200.0);
        when(accountService.getAccountsByBeneficiaryId(beneficiaryId)).thenReturn(List.of(account1, account2));

        double maxWithdrawal = accountManagementService.getMaxWithdrawalLastMonth(beneficiaryId);

        // Assert
        assertThat(maxWithdrawal).isEqualTo(200.0);
        verify(transactionService, times(1)).getMaxWithdrawalLastMonth(accountIds);
    }
}
