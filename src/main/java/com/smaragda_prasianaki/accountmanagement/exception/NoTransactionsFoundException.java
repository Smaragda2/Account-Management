package com.smaragda_prasianaki.accountmanagement.exception;

public class NoTransactionsFoundException extends RuntimeException {
    public static final String ERROR_MESSAGE = "No withdrawal transactions found for the last month for ";
    public NoTransactionsFoundException(String beneficiaryId) {
        super(ERROR_MESSAGE + beneficiaryId);
    }
}
