package com.portal.bid.controller;

import com.portal.bid.dto.OpportunitiesResponse;
import com.portal.bid.entity.Form;
import com.portal.bid.entity.User;
import com.portal.bid.service.EmailService;
import com.portal.bid.service.OpportunityService;
import com.portal.bid.service.UserService;
import com.portal.bid.service.implementation.OpportunityServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opportunities")
public class FormController {

    @Autowired
    private OpportunityServiceImp opportunityService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    // Create Opportunity
    @PostMapping
    public ResponseEntity<?> createOpportunity(@RequestBody Form opportunity, @RequestParam(defaultValue = "false") boolean forcecreate) {
        // Set the current date for createdAt and updatedAt
        LocalDateTime now = LocalDateTime.now();
        opportunity.setCreatedAt(now);
        opportunity.setUpdatedAt(now);

        // Fetch the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Assuming email is used as the principal

        // Set the createdBy and updatedBy fields with the current user's email
        opportunity.setCreatedBy(currentUserEmail);
        opportunity.setUpdatedBy(currentUserEmail);

        if (!forcecreate) {
            List<Form> potentialDuplicates = opportunityService.findPotentialDuplicates(opportunity);
            if (!potentialDuplicates.isEmpty()) {
                List<DuplicateOpportunityResponse.PotentialDuplicate> duplicateList = potentialDuplicates.stream()
                        .map(form -> new DuplicateOpportunityResponse.PotentialDuplicate(
                                form.getId(),
                                form.getOpportunity(),
                                form.getSubmissionDate(),
                                form.getBusinessUnit(),
                                form.getObFy(),
                                form.getObQtr()
                        ))
                        .collect(Collectors.toList());

                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new DuplicateOpportunityResponse("Potential duplicate opportunities found", duplicateList));
            }
        }

        // Save the opportunity if no duplicates found
        Form savedOpportunity = opportunityService.saveOpportunity(opportunity);

        // Extract primary owner details
        String primaryOwnerFullName = savedOpportunity.getPrimaryOwner();
        String[] nameParts = primaryOwnerFullName.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Fetch the primary owner's email
        User primaryOwner = userService.getUserByFirstAndLastName(firstName, lastName);
        String primaryOwnerEmail = primaryOwner.getEmail();

        // Construct the email content
        String subject = "New Opportunity Created by " + currentUserEmail;
        String body = String.format("Dear %s,\n\nA new opportunity has been created.\n\n" +
                        "Opportunity ID: %s\n" +
                        "Title: %s\n" +
                        "Description: %s\n" +
                        "Status: %s\n" +
                        "Created by: %s\n\n" +
                        "Please review the details at your earliest convenience.\n\n" +
                        "Best regards,\nBid-Portal Team",
                primaryOwnerFullName, savedOpportunity.getId(), savedOpportunity.getOpportunity(),
                savedOpportunity.getIndustrySegment(), savedOpportunity.getDealStatus(), currentUserEmail);

        emailService.sendEmail(primaryOwnerEmail, subject, body);

        return ResponseEntity.ok(savedOpportunity);
    }

    // Update Opportunity
    @PutMapping("/{id}")
    public ResponseEntity<Form> updateOpportunity(@PathVariable Long id, @RequestBody Form updatedOpportunity) {
        // Set the current user as the updatedBy (assuming the same field createdBy is used for tracking both create and update)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        updatedOpportunity.setUpdatedBy(currentUserEmail);
        updatedOpportunity.setUpdatedAt(LocalDateTime.now());

        Form updated = opportunityService.updateOpportunity(id, updatedOpportunity);
        System.out.println("hey put req");
        if (updated != null) {
            // Extract primary owner details
            String primaryOwnerFullName = updated.getPrimaryOwner();
            String[] nameParts = primaryOwnerFullName.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : null;

            // Fetch the primary owner's email
            User primaryOwner = userService.getUserByFirstAndLastName(firstName, lastName);
            String primaryOwnerEmail = primaryOwner.getEmail();

            // Construct the email content
            String subject = "Opportunity Updated by " + currentUserEmail;
            String body = String.format("Dear %s,\n\nThe opportunity has been updated.\n\n" +
                            "Opportunity ID: %s\n" +
                            "Title: %s\n" +
                            "Description: %s\n" +
                            "Status: %s\n" +
                            "Updated by: %s\n\n" +
                            "Please review the updated details at your earliest convenience.\n\n" +
                            "Best regards,\nBid-Portal Team",
                    primaryOwnerFullName, updated.getId(), updated.getOpportunity(),
                    updated.getIndustrySegment(), updated.getDealStatus(), currentUserEmail);

            emailService.sendEmail(primaryOwnerEmail, subject, body);

            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all opportunities
    @GetMapping
    public ResponseEntity<List<Form>> getAllOpportunities() {
        List<Form> opportunities = opportunityService.getAllOpportunities();
        return ResponseEntity.ok(opportunities);
    }

    // Get opportunity by ID
    @GetMapping("/{id}")
    public ResponseEntity<Form> getOpportunityById(@PathVariable Long id) {
        Form opportunity = opportunityService.getOpportunityById(id);
        return opportunity != null ? ResponseEntity.ok(opportunity) : ResponseEntity.notFound().build();
    }

    // Filtered opportunities
    @GetMapping("/filtered")
    public ResponseEntity<?> getOpportunities(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String ob_fy,
            @RequestParam(required = false) String business_unit,
            @RequestParam(required = false) String industry_segment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
            @RequestParam(required = false) String responsible_person,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) BigDecimal deal_value_min,
            @RequestParam(required = false) BigDecimal deal_value_max) {

        List<Form> filteredOpportunities = opportunityService.getFilteredOpportunities(
                status, priority, ob_fy, business_unit, industry_segment, start_date, end_date,
                responsible_person, customer, deal_value_min, deal_value_max);

        int totalOpportunities = filteredOpportunities.size();
        return ResponseEntity.ok(new OpportunitiesResponse(totalOpportunities, filteredOpportunities));
    }
}

