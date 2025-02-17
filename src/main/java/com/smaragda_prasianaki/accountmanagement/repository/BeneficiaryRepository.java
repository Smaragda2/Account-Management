package com.smaragda_prasianaki.accountmanagement.repository;

import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class BeneficiaryRepository implements ICsvRepository<Beneficiary> {
    private static final String FILEPATH = "src/main/resources/beneficiaries.csv";

    @Override
    public List<Beneficiary> loadData() {
        try {
            return loadData(FILEPATH, Beneficiary.class);
        } catch (RuntimeException e) {
            log.error("Couldn't load data from beneficiaries.csv. Reason: {}", e.getMessage());
            throw e;
        }
    }
}
