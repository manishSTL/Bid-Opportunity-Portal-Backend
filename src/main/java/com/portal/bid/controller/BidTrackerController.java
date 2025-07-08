package com.portal.bid.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bid.dto.BidTrackerDTO;
import com.portal.bid.entity.User;
import com.portal.bid.service.BidTrackerService;
import com.portal.bid.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bid-trackers")
@Validated
@Tag(name = "Bid Tracker Management", description = "APIs for managing bid tracking workflow")
public class BidTrackerController {

    @Autowired
    private BidTrackerService bidTrackerService;
    
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all bid trackers", description = "Retrieves all bid trackers in the system")
    @GetMapping
    public ResponseEntity<List<BidTrackerDTO>> getAllBidTrackers() {
        return ResponseEntity.ok(bidTrackerService.getAllBidTrackers());
    }

    @Operation(summary = "Get bid tracker by ID", description = "Retrieves a specific bid tracker by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BidTrackerDTO> getBidTrackerById(@PathVariable Long id) {
        return ResponseEntity.ok(bidTrackerService.getBidTrackerById(id));
    }

    @Operation(summary = "Get bid tracker by lead ID", description = "Retrieves the bid tracker associated with a specific lead")
    @GetMapping("/lead/{leadId}")
    public ResponseEntity<BidTrackerDTO> getBidTrackerByLeadId(@PathVariable Long leadId) {
        return ResponseEntity.ok(bidTrackerService.getBidTrackerByLeadId(leadId));
    }

    @Operation(summary = "Update bid tracker", description = "Updates an existing bid tracker with new values")
    @PutMapping("/{id}")
    public ResponseEntity<BidTrackerDTO> updateBidTracker(
            @PathVariable Long id,
            @Valid @RequestBody BidTrackerDTO bidTrackerDTO) {
        
        // Get authenticated user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userService.findUserByEmail(currentUserEmail);
        System.out.println("inside controller ------------------------------                 1");
        return ResponseEntity.ok(bidTrackerService.updateBidTracker(id, bidTrackerDTO, currentUser));
    }
}