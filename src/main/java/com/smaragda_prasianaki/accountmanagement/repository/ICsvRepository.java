package com.smaragda_prasianaki.accountmanagement.repository;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public interface ICsvRepository<T> {
    // Abstract method to be implemented by concrete repositories
    List<T> loadData();

    // Default method with common logic for loading CSV data
    default List<T> loadData(String filePath, Class<T> clazz) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from CSV file: " + filePath, e);
        }
    }
}
