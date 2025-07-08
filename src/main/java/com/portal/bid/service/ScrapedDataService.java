package com.portal.bid.service;

import com.portal.bid.entity.ScrapedData;
import com.portal.bid.repository.ScrapedDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScrapedDataService {

    @Autowired
    private ScrapedDataRepository scrapedDataRepository;

    @Transactional
    public void processAndStore(ScrapedData data) {
        // Add any necessary processing logic here
        System.out.println(data.getTenderId());
        // If the tenderId is not provided, generate one
        if (data.getTenderId() == null || data.getTenderId().isEmpty()) {
            data.setTenderId(generateTenderId());
        }

        // Save the data to the database
        scrapedDataRepository.save(data);
    }

    private String generateTenderId() {
        // Implement logic to generate a unique tender ID
        return "TEN" + System.currentTimeMillis();
    }

    // Find a record by ID
    public Optional<ScrapedData> findById(Long id) {
        return scrapedDataRepository.findById(id);
    }

    // Find all records
    public List<ScrapedData> findAll() {
        return scrapedDataRepository.findAll();
    }

    // Save or update a record
    public ScrapedData save(ScrapedData data) {
        return scrapedDataRepository.save(data);
    }

    // New methods for summary data
    public long countByPublishedDate(LocalDate date) {
        return scrapedDataRepository.countByPublishedDate(date);
    }

    public long countByPublishedDateBetween(LocalDate startDate, LocalDate endDate) {
        return scrapedDataRepository.countByPublishedDateBetween(startDate, endDate);
    }

    public long countByClosingDateBetween(LocalDate startDate, LocalDate endDate) {
        return scrapedDataRepository.countByClosingDateBetween(startDate, endDate);
    }

    public long countByCurrentQuarter() {
        LocalDate now = LocalDate.now();
        LocalDate startOfQuarter = now.withDayOfMonth(1).minusMonths((now.getMonthValue() - 1) % 3);
        LocalDate endOfQuarter = startOfQuarter.plusMonths(3).minusDays(1);
        return scrapedDataRepository.countByPublishedDateBetween(startOfQuarter, endOfQuarter);
    }

    // New method for filtered search
    public List<ScrapedData> findAllWithFilters(
            Double amountMin, Double amountMax,
            LocalDate publishedDateStart, LocalDate publishedDateEnd,
            LocalDate closingDateStart, LocalDate closingDateEnd,
            String organization, String keyword) {

        Specification<ScrapedData> spec = Specification.where(null);

        if (amountMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), amountMin));
        }
        if (amountMax != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("amount"), amountMax));
        }
        if (publishedDateStart != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("publishedDate"), publishedDateStart));
        }
        if (publishedDateEnd != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("publishedDate"), publishedDateEnd));
        }
        if (closingDateStart != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("closingDate"), closingDateStart));
        }
        if (closingDateEnd != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("closingDate"), closingDateEnd));
        }
        if (organization != null && !organization.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("organisation")), "%" + organization.toLowerCase() + "%"));
        }
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("keyword")), "%" + keyword.toLowerCase() + "%"));
        }

        return scrapedDataRepository.findAll(spec);
    }
}