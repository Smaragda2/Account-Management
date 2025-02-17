package com.smaragda_prasianaki.accountmanagement.repository;

import com.smaragda_prasianaki.accountmanagement.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class AccountRepository implements ICsvRepository<Account> {
    private static final String FILEPATH = "src/main/resources/accounts.csv";

    @Override
    public List<Account> loadData() {
        try {
            return loadData(FILEPATH, Account.class);
        } catch (RuntimeException e) {
            log.error("Couldn't load data from accounts.csv. Reason: {}", e.getMessage());
            throw e;
        }
    }
}
