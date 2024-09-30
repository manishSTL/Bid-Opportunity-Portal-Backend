package com.portal.bid.controller;

import com.portal.bid.entity.ScrapedData;
import com.portal.bid.service.ScrapedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrape")
public class ScrapedDataController {

    private final ScrapedDataService scrapedDataService;

    @Autowired
    public ScrapedDataController(ScrapedDataService scrapedDataService) {
        this.scrapedDataService = scrapedDataService;
    }

    @PostMapping("/receive")
    @PreAuthorize("hasRole('ROLE_API')")
    public ResponseEntity<?> receiveScrapedData(@RequestBody ScrapedData data) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current authentication: " + auth);
        System.out.println("Authorities: " + auth.getAuthorities());

        try {
            scrapedDataService.processAndStore(data);
            return ResponseEntity.ok("Data received and processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing data: " + e.getMessage());
        }
    }
}