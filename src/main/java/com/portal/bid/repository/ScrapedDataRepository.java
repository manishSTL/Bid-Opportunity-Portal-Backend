package com.portal.bid.repository;

import com.portal.bid.entity.ScrapedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ScrapedDataRepository extends JpaRepository<ScrapedData, Long>, JpaSpecificationExecutor<ScrapedData> {
    long countByPublishedDate(LocalDate date);
    long countByPublishedDateBetween(LocalDate startDate, LocalDate endDate);
    long countByClosingDateBetween(LocalDate startDate, LocalDate endDate);
}
