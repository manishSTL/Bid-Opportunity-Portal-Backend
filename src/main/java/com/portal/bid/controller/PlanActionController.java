package com.portal.bid.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bid.dto.LeadResponseDTO;
import com.portal.bid.entity.PlanAction;
import com.portal.bid.entity.User;
import com.portal.bid.service.EmailService;
import com.portal.bid.service.LeadService;
import com.portal.bid.service.PlanActionService;
import com.portal.bid.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/plans")
public class PlanActionController {

    @Autowired
    private PlanActionService planService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private LeadService formService; // To fetch form details

    @PostMapping
    public ResponseEntity<PlanAction> createPlan(@Valid @RequestBody PlanAction plan) {
        plan.setCreatedAt(LocalDateTime.now());
        PlanAction createdPlan = planService.createPlan(plan);

        // Fetch the form details using form_id from PlanAction
        Long formId = plan.getFormId();  // Assuming formId is available in PlanAction
        LeadResponseDTO form = formService.getLeadById(formId);

        if (form == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Fetch the primary owner from the form
        String primaryOwnerFullName = form.getPrimaryOwner();
        if (primaryOwnerFullName == null || primaryOwnerFullName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle missing primary owner
        }

        // Fetch the primary owner's email
        String[] nameParts = primaryOwnerFullName.split("\\s+");
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : null;
        User primaryOwner = userService.getUserByFirstAndLastName(firstName, lastName);
        if (primaryOwner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle case if primary owner is not found
        }

        String primaryOwnerEmail = primaryOwner.getEmail();

        // Fetch the current user who created the plan
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Assuming email is the principal

        // Construct the email content
        String subject = "New Plan Created for Opportunity ID: " + formId + " by " + currentUserEmail;
        String body = String.format("Dear %s,\n\nA new plan has been created for the opportunity.\n\n" +
                        "Opportunity ID: %s\n" +
                        "Plan ID: %s\n" +
                        "Plan Title: %s\n" +
                        "Plan Details: %s\n" +
                        "Created by: %s\n\n" +
                        "Please review the plan details at your earliest convenience.\n\n" +
                        "Best regards,\nBid-Portal Team",
                primaryOwnerFullName, formId, createdPlan.getId(), createdPlan.getAction(),
                createdPlan.getPlan(), currentUserEmail);

        // emailService.sendEmail(primaryOwnerEmail, subject, body);

        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanAction> updatePlan(@PathVariable Long id,@Valid @RequestBody PlanAction plan) {
        PlanAction updatedPlan = planService.updatePlan(id, plan);

        if (updatedPlan != null) {
            // Fetch the form details using form_id from PlanAction
            Long formId = updatedPlan.getFormId();
            System.out.println("formid: "+formId);
            LeadResponseDTO form = formService.getLeadById(formId);

            if (form == null) {
                System.out.println("here1");

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Fetch the primary owner from the form
            String primaryOwnerFullName = form.getPrimaryOwner();
            if (primaryOwnerFullName == null || primaryOwnerFullName.isEmpty()) {
                System.out.println("here2");

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Fetch the primary owner's email
            // Extract primary owner details
            String[] nameParts = primaryOwnerFullName.split("\\s+");
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : null;

            System.out.println(firstName);
            System.out.println(lastName);
            User primaryOwner = userService.getUserByFirstAndLastName(firstName, lastName);
            System.out.println(primaryOwner);
            if (primaryOwner == null) {
                System.out.println("here3");

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String primaryOwnerEmail = primaryOwner.getEmail();

            // Fetch the current user who updated the plan
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            // Construct the email content
            String subject = "Plan Updated for Opportunity ID: " + formId + " by " + currentUserEmail;
            String body = String.format("Dear %s,\n\nThe plan for the opportunity has been updated.\n\n" +
                            "Opportunity ID: %s\n" +
                            "Plan ID: %s\n" +
                            "Plan Title: %s\n" +
                            "Plan Details: %s\n" +
                            "Updated by: %s\n\n" +
                            "Please review the updated plan details at your earliest convenience.\n\n" +
                            "Best regards,\nBid-Portal Team",
                    primaryOwnerFullName, formId, updatedPlan.getId(), updatedPlan.getAction(),
                    updatedPlan.getPlan(), currentUserEmail);

            // emailService.sendEmail(primaryOwnerEmail, subject, body);

            return new ResponseEntity<>(updatedPlan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<PlanAction>> getAllPlans(@RequestParam(required = false) Long form_id) {

        List<PlanAction> plans;

        if (form_id != null) {

            plans = planService.getAllPlansByFormId(form_id);

        } else {

            plans = planService.getAllPlans();

        }

        return new ResponseEntity<>(plans, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanAction> getPlanById(@PathVariable Long id) {
        PlanAction plan = planService.getPlanById(id);
        return plan != null ? ResponseEntity.ok(plan) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}




