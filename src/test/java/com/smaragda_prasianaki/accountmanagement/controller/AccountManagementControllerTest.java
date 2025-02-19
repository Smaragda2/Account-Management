package com.smaragda_prasianaki.accountmanagement.controller;

import com.smaragda_prasianaki.accountmanagement.dto.AccountBalanceDTO;
import com.smaragda_prasianaki.accountmanagement.dto.BalanceDTO;
import com.smaragda_prasianaki.accountmanagement.dto.MaxWithdrawDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import com.smaragda_prasianaki.accountmanagement.service.AccountManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AccountManagementService accountManagementService;
    @InjectMocks
    private AccountManagementController accountManagementController;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountManagementController).build();
    }

    @Test
    void testGetBeneficiaryById() throws Exception {
        // Arrange
        String beneficiaryId = "1";
        Beneficiary beneficiary = new Beneficiary(beneficiaryId, "John", "Doe");

        when(accountManagementService.getBeneficiaryById(beneficiaryId))
                .thenReturn(beneficiary);

        // Act & Assert
        mockMvc.perform(get("/api/beneficiaries/{beneficiaryId}", beneficiaryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beneficiaryId").value(beneficiary.getBeneficiaryId()))
                .andExpect(jsonPath("$.firstName").value(beneficiary.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(beneficiary.getLastName()));
    }

    @Test
    void testGetAccountsByBeneficiaryId() throws Exception {
        // Arrange
        String beneficiaryId = "1";
        Account account1 = new Account("1", beneficiaryId);
        Account account2 = new Account("2", beneficiaryId);

        when(accountManagementService.getAccountsByBeneficiaryId(beneficiaryId))
                .thenReturn(List.of(account1, account2));

        // Act & Assert
        mockMvc.perform(get("/api/beneficiaries/{beneficiaryId}/accounts", beneficiaryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountId").value(account1.getAccountId()))
                .andExpect(jsonPath("$[0].beneficiaryId").value(account1.getBeneficiaryId()))
                .andExpect(jsonPath("$[1].accountId").value(account2.getAccountId()))
                .andExpect(jsonPath("$[1].beneficiaryId").value(account2.getBeneficiaryId()));
    }

    @Test
    void testGetTransactionsByBeneficiaryId() throws Exception {
        // Arrange
        String beneficiaryId = "1";
        Transaction transaction1 = new Transaction("1", "1", 100.0, TransactionType.DEPOSIT, "10/01/25");
        Transaction transaction2 = new Transaction("2", "1", 50.0, TransactionType.WITHDRAWAL, "10/02/25");
        Transaction transaction3 = new Transaction("3", "2", 50.0, TransactionType.DEPOSIT, "10/03/25");

        when(accountManagementService.getTransactionsByBeneficiaryId(beneficiaryId))
                .thenReturn(List.of(transaction1, transaction2, transaction3));

        // Act & Assert
        mockMvc.perform(get("/api/beneficiaries/{beneficiaryId}/transactions", beneficiaryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value(transaction1.getTransactionId()))
                .andExpect(jsonPath("$[0].accountId").value(transaction1.getAccountId()))
                .andExpect(jsonPath("$[1].transactionId").value(transaction2.getTransactionId()))
                .andExpect(jsonPath("$[1].accountId").value(transaction2.getAccountId()))
                .andExpect(jsonPath("$[2].transactionId").value(transaction3.getTransactionId()))
                .andExpect(jsonPath("$[2].accountId").value(transaction3.getAccountId()));
    }

    @Test
    void testGetBalancesByBeneficiaryId() throws Exception {
        // Arrange
        String beneficiaryId = "1";
        AccountBalanceDTO balance1 = new AccountBalanceDTO("1", 50.0);
        AccountBalanceDTO balance2 = new AccountBalanceDTO("2", 125.0);
        BalanceDTO balanceDTO = new BalanceDTO(List.of(balance1, balance2), 175.0);

        when(accountManagementService.getBalancesByBeneficiaryId(beneficiaryId))
                .thenReturn(balanceDTO);

        // Act & Assert
        mockMvc.perform(get("/api/beneficiaries/{beneficiaryId}/balance", beneficiaryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalances[0].accountId").value(balance1.getAccountId()))
                .andExpect(jsonPath("$.accountBalances[0].balance").value(balance1.getBalance()))
                .andExpect(jsonPath("$.accountBalances[1].accountId").value(balance2.getAccountId()))
                .andExpect(jsonPath("$.accountBalances[1].balance").value(balance2.getBalance()))
                .andExpect(jsonPath("$.totalBalance").value(balanceDTO.getTotalBalance()));
    }

    @Test
    void testGetMaxWithdrawalLastMonth() throws Exception {
        // Arrange
        String beneficiaryId = "1";
        String lastMonthDate = LocalDate.now().minusMonths(1).format(DATE_FORMATTER);

        MaxWithdrawDTO maxWithdrawDTO = new MaxWithdrawDTO(75.0, lastMonthDate);

        when(accountManagementService.getMaxWithdrawalLastMonth(beneficiaryId))
                .thenReturn(maxWithdrawDTO);

        // Act & Assert
        mockMvc.perform(get("/api/beneficiaries/{beneficiaryId}/transactions/maxWithdrawLastMonth", beneficiaryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxWithdraw").value(maxWithdrawDTO.getMaxWithdraw()))
                .andExpect(jsonPath("$.date").value(maxWithdrawDTO.getDate()));
    }
}
