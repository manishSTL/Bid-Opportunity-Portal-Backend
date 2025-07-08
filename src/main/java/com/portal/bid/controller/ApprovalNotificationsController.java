package com.portal.bid.controller;

import com.portal.bid.dto.ApprovalNotificationDTO;
import com.portal.bid.entity.ApprovalNotifications;
import com.portal.bid.service.ApprovalNotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalNotificationsController {

    @Autowired
    private ApprovalNotificationsService service;

    @GetMapping
    public ResponseEntity<List<ApprovalNotificationDTO>> getApprovals(@RequestParam Long userId) {
        List<ApprovalNotificationDTO> approvals = service.getApprovalsByUserId(userId);
        return ResponseEntity.ok(approvals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalNotifications> getApprovalById(@PathVariable Long id) {
        return service.getApprovalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApprovalNotifications> createApproval(@RequestBody ApprovalNotifications approval) {
        ApprovalNotifications createdApproval = service.createApproval(approval);
        return ResponseEntity.ok(createdApproval);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApprovalNotifications> updateApproval(@PathVariable Long id, @RequestBody ApprovalNotifications approvalDetails) {
        ApprovalNotifications updatedApproval = service.updateApproval(id, approvalDetails);
        if (updatedApproval != null) {
            return ResponseEntity.ok(updatedApproval);
        }
        return ResponseEntity.notFound().build();
    }
}