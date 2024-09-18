package com.portal.bid.controller;

import com.portal.bid.entity.Form;
import com.portal.bid.entity.PlanAction;
import com.portal.bid.entity.User;
import com.portal.bid.service.EmailService;
import com.portal.bid.service.OpportunityService;
import com.portal.bid.service.OpportunityService;
import com.portal.bid.service.PlanActionService;
import com.portal.bid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private OpportunityService formService; // To fetch form details

    @PostMapping
    public ResponseEntity<PlanAction> createPlan(@RequestBody PlanAction plan) {
        plan.setCreatedAt(LocalDateTime.now());
        PlanAction createdPlan = planService.createPlan(plan);

        // Fetch the form details using form_id from PlanAction
        Long formId = plan.getFormId();  // Assuming formId is available in PlanAction
        Form form = formService.getOpportunityById(formId);

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

        emailService.sendEmail(primaryOwnerEmail, subject, body);

        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanAction> updatePlan(@PathVariable Long id, @RequestBody PlanAction plan) {
        PlanAction updatedPlan = planService.updatePlan(id, plan);

        if (updatedPlan != null) {
            // Fetch the form details using form_id from PlanAction
            Long formId = updatedPlan.getFormId();
            System.out.println("formid: "+formId);
            Form form = formService.getOpportunityById(formId);

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

            emailService.sendEmail(primaryOwnerEmail, subject, body);

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
}




