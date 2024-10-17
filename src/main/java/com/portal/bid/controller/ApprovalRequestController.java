package com.portal.bid.controller;

import com.portal.bid.dto.ApprovalRequestDTO;
import com.portal.bid.dto.ApprovalNotificationDTO;
import com.portal.bid.dto.ApprovalFormInfoDTO;

import com.portal.bid.service.ApprovalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/approval-requests")
public class ApprovalRequestController {

    @Autowired
    private ApprovalRequestService approvalRequestService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createApprovalRequest(
            @RequestPart("file") MultipartFile file,
            @RequestPart("formId") String formId,
            @RequestPart("primaryOwner") String primaryOwner) {
        try {
            Long formIdLong = Long.parseLong(formId);
            ApprovalRequestDTO createdRequest = approvalRequestService.createApprovalRequest(file, formIdLong, primaryOwner);
            return ResponseEntity.ok(createdRequest);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid formId: " + formId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating approval request: " + e.getMessage());
        }
    }

    @GetMapping("/check-action/{formId}")
    public ResponseEntity<?> checkActionPerformed(@PathVariable Long formId) {
        try {
            System.out.println("Form ID received: " + formId);
            String actionPerformed = approvalRequestService.checkActionPerformed(formId);

            return ResponseEntity.ok(actionPerformed);
        } catch (RuntimeException e) {
            // Return 404 Not Found if the approval request is missing
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Approval request not found for formId: " + formId);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking action performed: " + e.getMessage());
        }
    }


    @GetMapping("/notifications/{formId}")
    public ResponseEntity<?> getApprovalNotifications(@PathVariable Long formId) {
        try {
            System.out.println("pppppppppppppppppp+ "+formId);
            List<ApprovalNotificationDTO> notifications = approvalRequestService.getApprovalNotifications(formId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching approval notifications: " + e.getMessage());
        }
    }

    @GetMapping("/all-form-info")
    public ResponseEntity<List<ApprovalFormInfoDTO>> getAllApprovalFormInfo() {
        try {
            List<ApprovalFormInfoDTO> approvalFormInfoList = approvalRequestService.getAllApprovalFormInfo();
            return ResponseEntity.ok(approvalFormInfoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}