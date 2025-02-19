package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.dto.AccountBalanceDTO;
import com.smaragda_prasianaki.accountmanagement.dto.BalanceDTO;
import com.smaragda_prasianaki.accountmanagement.dto.MaxWithdrawDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountManagementServiceTest {
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private BeneficiaryService beneficiaryService;

    @InjectMocks
    private AccountManagementService accountManagementService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

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
        assertThat(accounts.get(0).getAccountId()).isEqualTo(account1.getAccountId());
        assertThat(accounts.get(1).getAccountId()).isEqualTo(account2.getAccountId());
        verify(accountService, times(1)).getAccountsByBeneficiaryId(beneficiaryId);
    }

    @Test
    void testGetTransactionsByBeneficiaryId() {
        when(transactionService.getTransactionsByAccountIds(accountIds)).thenReturn(List.of(transaction1, transaction2));
        when(accountService.getAccountsByBeneficiaryId(beneficiaryId)).thenReturn(List.of(account1, account2));

        List<Transaction> transactions = accountManagementService.getTransactionsByBeneficiaryId(beneficiaryId);

        // Assert
        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getTransactionId()).isEqualTo(transaction1.getTransactionId());
        assertThat(transactions.get(1).getTransactionId()).isEqualTo(transaction2.getTransactionId());
        verify(transactionService, times(1)).getTransactionsByAccountIds(accountIds);
    }

    @Test
    void testGetBalancesByBeneficiaryId() {
        BalanceDTO balanceDTO = new BalanceDTO(List.of(balance1, balance2), balance1.getBalance() + balance2.getBalance());
        when(accountService.getBalancesByBeneficiaryId(beneficiaryId)).thenReturn(balanceDTO);

        BalanceDTO balance = accountManagementService.getBalancesByBeneficiaryId(beneficiaryId);

        // Assert
        assertThat(balance.getAccountBalances()).hasSize(2);
        assertThat(balance.getAccountBalances().get(0).getAccountId()).isEqualTo(balance1.getAccountId());
        assertThat(balance.getAccountBalances().get(0).getBalance()).isEqualTo(balance1.getBalance());
        assertThat(balance.getAccountBalances().get(1).getAccountId()).isEqualTo(balance2.getAccountId());
        assertThat(balance.getAccountBalances().get(1).getBalance()).isEqualTo(balance2.getBalance());
        assertThat(balance.getTotalBalance()).isEqualTo(balance1.getBalance() + balance2.getBalance());
        verify(accountService, times(1)).getBalancesByBeneficiaryId(beneficiaryId);
    }

    @Test
    void testGetMaxWithdrawalLastMonth() {
        String lastMonthDate = LocalDate.now().minusMonths(1).format(DATE_FORMATTER);
        MaxWithdrawDTO maxWithdrawDTO = new MaxWithdrawDTO(200.0, lastMonthDate);

        when(transactionService.getMaxWithdrawalLastMonth(accountIds, beneficiaryId)).thenReturn(maxWithdrawDTO);
        when(accountService.getAccountsByBeneficiaryId(beneficiaryId)).thenReturn(List.of(account1, account2));

        MaxWithdrawDTO maxWithdrawal = accountManagementService.getMaxWithdrawalLastMonth(beneficiaryId);

        // Assert
        assertThat(maxWithdrawal.getMaxWithdraw()).isEqualTo(maxWithdrawDTO.getMaxWithdraw());
        assertThat(maxWithdrawal.getDate()).isEqualTo(maxWithdrawDTO.getDate());
        verify(transactionService, times(1)).getMaxWithdrawalLastMonth(accountIds, beneficiaryId);
    }
}
