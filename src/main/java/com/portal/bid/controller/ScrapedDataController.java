package com.portal.bid.controller;

import com.portal.bid.entity.ScrapedData;
import com.portal.bid.service.ScrapedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/scrape")
public class ScrapedDataController {

    private final ScrapedDataService scrapedDataService;

    @Autowired
    public ScrapedDataController(ScrapedDataService scrapedDataService) {
        this.scrapedDataService = scrapedDataService;
    }

    // Create (handling single and multiple records)
    @PostMapping("/receive")
    @PreAuthorize("hasRole('ROLE_API')")
    public ResponseEntity<?> receiveScrapedData(@RequestBody List<ScrapedData> dataList) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current authentication: " + auth);
        System.out.println("Authorities: " + auth.getAuthorities());

        try {
            for (ScrapedData data : dataList) {
                String qtr = calculateQuarter(data.getPublishedDate());
                String fy = calculateFiscalYear(data.getPublishedDate());

                data.setQTR(qtr);
                data.setFY(fy);
                data.setCreatedAt(LocalDateTime.now());

                scrapedDataService.processAndStore(data);
            }
            return ResponseEntity.ok("Data received and processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing data: " + e.getMessage());
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummaryData() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusWeeks(1);
            LocalDate closingSoonThreshold = today.plusDays(7); // Assuming "closing soon" means within a week

            long newToday = scrapedDataService.countByPublishedDate(today);
            long newThisWeek = scrapedDataService.countByPublishedDateBetween(weekAgo, today);
            long closingSoon = scrapedDataService.countByClosingDateBetween(today, closingSoonThreshold);
            long currentQtrTotal = scrapedDataService.countByCurrentQuarter();

            Map<String, Long> summaryData = new HashMap<>();
            summaryData.put("newToday", newToday);
            summaryData.put("newThisWeek", newThisWeek);
            summaryData.put("closingSoon", closingSoon);
            summaryData.put("currentQtrTotal", currentQtrTotal);

            return ResponseEntity.ok(summaryData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching summary data: " + e.getMessage());
        }
    }

    // Modify the existing getAllScrapedData method to accept filter parameters
    @GetMapping("/all")
    public ResponseEntity<?> getAllScrapedData(
            @RequestParam(required = false) Double amountMin,
            @RequestParam(required = false) Double amountMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedDateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedDateEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate closingDateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate closingDateEnd,
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) String keyword
    ) {
        try {
            List<ScrapedData> dataList = scrapedDataService.findAllWithFilters(
                    amountMin, amountMax, publishedDateStart, publishedDateEnd,
                    closingDateStart, closingDateEnd, organization, keyword
            );
            if (dataList.isEmpty()) {
                return ResponseEntity.ok("No data found");
            }
            return ResponseEntity.ok(dataList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching data: " + e.getMessage());
        }
    }

    // Read (get by ID)
    @GetMapping("/{id}")
    public ResponseEntity<?> getScrapedDataById(@PathVariable Long id) {
        try {
            Optional<ScrapedData> data = scrapedDataService.findById(id);
            if (data.isPresent()) {
                return ResponseEntity.ok(data.get());
            } else {
                return ResponseEntity.badRequest().body("No data found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching data: " + e.getMessage());
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateScrapedData(@PathVariable Long id, @RequestBody ScrapedData updatedData) {
        try {
            Optional<ScrapedData> existingDataOpt = scrapedDataService.findById(id);
            if (existingDataOpt.isPresent()) {
                ScrapedData existingData = existingDataOpt.get();

                existingData.setWebsiteUrl(updatedData.getWebsiteUrl());
                existingData.setPlaceDetails(updatedData.getPlaceDetails());
                existingData.setOrganisation(updatedData.getOrganisation());
                existingData.setTenderId(updatedData.getTenderId());
                existingData.setAmount(updatedData.getAmount());
                existingData.setEmd(updatedData.getEmd());
                existingData.setPublishedDate(updatedData.getPublishedDate());
                existingData.setClosingDate(updatedData.getClosingDate());
                existingData.setTitle(updatedData.getTitle());
                existingData.setDescription(updatedData.getDescription());
                existingData.setKeyword(updatedData.getKeyword());

                // Recalculate QTR and FY for the updated record
                String qtr = calculateQuarter(updatedData.getPublishedDate());
                String fy = calculateFiscalYear(updatedData.getPublishedDate());
                existingData.setQTR(qtr);
                existingData.setFY(fy);

                scrapedDataService.processAndStore(existingData);
                return ResponseEntity.ok("Data updated successfully");
            } else {
                return ResponseEntity.badRequest().body("No data found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating data: " + e.getMessage());
        }
    }

    // Helper methods for calculating QTR and FY
    private String calculateQuarter(LocalDate publishedDate) {
        int month = publishedDate.getMonthValue();
        if (month >= 1 && month <= 3) {
            return "Q4";
        } else if (month >= 4 && month <= 6) {
            return "Q1";
        } else if (month >= 7 && month <= 9) {
            return "Q2";
        } else {
            return "Q3";
        }
    }

    private String calculateFiscalYear(LocalDate publishedDate) {
        int month = publishedDate.getMonthValue();
        int year = publishedDate.getYear();
        if (month >= 1 && month <= 3) {
            return "FY" + String.valueOf(year).substring(2);  // Current fiscal year
        } else {
            return "FY" + String.valueOf(year + 1).substring(2);  // Next fiscal year
        }
    }
}
