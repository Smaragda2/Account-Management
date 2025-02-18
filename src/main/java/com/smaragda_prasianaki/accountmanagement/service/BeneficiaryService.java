package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.repository.BeneficiaryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BeneficiaryService {
    private final BeneficiaryRepository beneficiaryRepository;
    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    private ConcurrentMap<String, Beneficiary> beneficiariesMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadData() {
        beneficiariesMap = beneficiaryRepository.loadData()
                .stream()
                .collect(Collectors.toConcurrentMap(Beneficiary::getBeneficiaryId, Function.identity()));
    }

    public Beneficiary getBeneficiaryById(String beneficiaryId) {
        return beneficiariesMap.get(beneficiaryId);
    }
}
