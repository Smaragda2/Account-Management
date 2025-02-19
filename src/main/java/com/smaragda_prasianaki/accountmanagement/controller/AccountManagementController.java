package com.smaragda_prasianaki.accountmanagement.controller;

import com.smaragda_prasianaki.accountmanagement.dto.BalanceDTO;
import com.smaragda_prasianaki.accountmanagement.dto.MaxWithdrawDTO;
import com.smaragda_prasianaki.accountmanagement.exception.NoTransactionsFoundException;
import com.smaragda_prasianaki.accountmanagement.model.Account;
import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import com.smaragda_prasianaki.accountmanagement.service.AccountManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Beneficiaries", description = "APIs related to Beneficiaries")
@RestController
@RequestMapping("/api/beneficiaries")
public class AccountManagementController {
    private final AccountManagementService accountManagementService;

    public AccountManagementController(AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }

    @Operation(summary = "Get Beneficiary Details",
            description = "Retrieve details of a beneficiary by ID",
            parameters = {
                @Parameter(name = "beneficiaryId", description = "ID of the beneficiary", example = "1")
            }
    )
    @GetMapping("/{beneficiaryId}")
    public Beneficiary getBeneficiary(@PathVariable String beneficiaryId) {
        return accountManagementService.getBeneficiaryById(beneficiaryId);
    }

    @Operation(summary = "Get Accounts for a Beneficiary",
            description = "Retrieve all accounts associated with a beneficiary ID",
            parameters = {
                @Parameter(name = "beneficiaryId", description = "ID of the beneficiary", example = "1")
            }
    )
    @GetMapping("/{beneficiaryId}/accounts")
    public List<Account> getAccountsByBeneficiary(@PathVariable String beneficiaryId) {
        return accountManagementService.getAccountsByBeneficiaryId(beneficiaryId);
    }

    @Operation(summary = "Get Transactions for a Beneficiary",
            description = "Retrieve all transactions associated to a beneficiary ID",
            parameters = {
                    @Parameter(name = "beneficiaryId", description = "ID of the beneficiary", example = "1")
            }
    )
    @GetMapping("/{beneficiaryId}/transactions")
    public List<Transaction> getTransactionsByBeneficiary(@PathVariable String beneficiaryId) {
        return accountManagementService.getTransactionsByBeneficiaryId(beneficiaryId);
    }

    @Operation(summary = "Get Balances for a Beneficiary",
            description = "Retrieve the balances for each account of a beneficiary and the total balance",
            parameters = {
                    @Parameter(name = "beneficiaryId", description = "ID of the beneficiary", example = "1")
            }
    )
    @GetMapping("/{beneficiaryId}/balance")
    public BalanceDTO getBalancesByBeneficiaryId(@PathVariable String beneficiaryId) {
        return accountManagementService.getBalancesByBeneficiaryId(beneficiaryId);
    }

    @Operation(summary = "Get Maximum Withdrawal in the Last Month",
            description = "Retrieve the maximum withdrawal amount for a beneficiary in the last month",
            parameters = {
                    @Parameter(name = "beneficiaryId", description = "ID of the beneficiary", example = "1")
            },
            responses = {
                @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
                @ApiResponse(responseCode = "404",
                        content = @Content(schema = @Schema(example = NoTransactionsFoundException.ERROR_MESSAGE + "${beneficiaryId}")) )
            }
    )
    @GetMapping("/{beneficiaryId}/transactions/maxWithdrawLastMonth")
    public MaxWithdrawDTO getMaxWithdrawalLastMonth(@PathVariable String beneficiaryId) {
        return accountManagementService.getMaxWithdrawalLastMonth(beneficiaryId);
    }
}
