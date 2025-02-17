package com.smaragda_prasianaki.accountmanagement.controller;

import com.smaragda_prasianaki.accountmanagement.AccountBalanceDTO;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.model.TransactionType;
import com.smaragda_prasianaki.accountmanagement.service.AccountManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountManagementController.class)
public class AccountManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private AccountManagementService accountManagementService;
    @InjectMocks
    private AccountManagementController accountManagementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
                .andExpect(jsonPath("$.beneficiaryId").value(beneficiaryId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
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
                .andExpect(jsonPath("$[0].accountId").value("1"))
                .andExpect(jsonPath("$[0].beneficiaryId").value(beneficiaryId))
                .andExpect(jsonPath("$[1].accountId").value("2"))
                .andExpect(jsonPath("$[1].beneficiaryId").value(beneficiaryId));
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
                .andExpect(jsonPath("$[0].transactionId").value("1"))
                .andExpect(jsonPath("$[0].accountId").value("1"))
                .andExpect(jsonPath("$[1].transactionId").value("2"))
                .andExpect(jsonPath("$[1].accountId").value("1"))
                .andExpect(jsonPath("$[2].transactionId").value("3"))
                .andExpect(jsonPath("$[1].accountId").value("2"));
    }

    @Test
    void testGetBalancesByBeneficiaryId() throws Exception {
        // Arrange
        String beneficiaryId = "1";
        AccountBalanceDTO balance1 = new AccountBalanceDTO("1", 50.0);
        AccountBalanceDTO balance2 = new AccountBalanceDTO("2", 125.0);

        when(accountManagementService.getBalancesByBeneficiaryId(beneficiaryId))
                .thenReturn(List.of(balance1, balance2));

        // Act & Assert
        mockMvc.perform(get("/api/beneficiaries/{beneficiaryId}/balance", beneficiaryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value("1"))
                .andExpect(jsonPath("$[0].balance").value(50.0))
                .andExpect(jsonPath("$[1].accountId").value("2"))
                .andExpect(jsonPath("$[1].balance").value(125.0));
    }

    @Test
    void testGetMaxWithdrawalLastMonth() throws Exception {
        // Arrange
        String beneficiaryId = "1";
        double maxWithdrawal = 75.0;

        when(accountManagementService.getMaxWithdrawalLastMonth(beneficiaryId))
                .thenReturn(maxWithdrawal);

        // Act & Assert
        mockMvc.perform(get("/api/beneficiaries/{beneficiaryId}/transactions/maxWithdrawalLastMonth", beneficiaryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("75.0"));
    }
}
