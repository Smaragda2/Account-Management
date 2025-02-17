package com.smaragda_prasianaki.accountmanagement.repository;

import com.smaragda_prasianaki.accountmanagement.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class TransactionRepository implements ICsvRepository<Transaction> {
    private static final String FILEPATH = "src/main/resources/transactions.csv";

    public List<Transaction> loadData() {
        try {
            return loadData(FILEPATH, Transaction.class);
        } catch (RuntimeException e) {
            log.error("Couldn't load data from transactions.csv. Reason: {}", e.getMessage());
            throw e;
        }
    }
}
