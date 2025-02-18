package com.smaragda_prasianaki.accountmanagement.service;

import com.smaragda_prasianaki.accountmanagement.model.Beneficiary;
import com.smaragda_prasianaki.accountmanagement.repository.BeneficiaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeneficiaryServiceTest {
    @Mock
    private BeneficiaryRepository beneficiaryRepository;
    @InjectMocks
    private BeneficiaryService beneficiaryService;

    @Test
    void testGetBeneficiaryById() {
        // Arrange
        Beneficiary beneficiary1 = new Beneficiary("1", "John", "Doe");
        Beneficiary beneficiary2 = new Beneficiary("2", "Jane", "Doe");

        when(beneficiaryRepository.loadData())
                .thenReturn(List.of(beneficiary1, beneficiary2));

        // Manually call loadData() to populate the map
        beneficiaryService.loadData();

        // Act
        Beneficiary result1 = beneficiaryService.getBeneficiaryById("1");
        Beneficiary result2 = beneficiaryService.getBeneficiaryById("2");
        Beneficiary result3 = beneficiaryService.getBeneficiaryById("3"); // Non-existent ID

        // Assert
        assertThat(result1).isEqualTo(beneficiary1);
        assertThat(result2).isEqualTo(beneficiary2);
        assertThat(result3).isNull(); // Non-existent ID should return null
    }
}
