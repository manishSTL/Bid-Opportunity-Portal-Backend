package com.portal.bid.service;

import com.portal.bid.dto.ApprovalNotificationDTO;
import com.portal.bid.entity.ApprovalNotifications;
import com.portal.bid.entity.ApprovalRequest;
import com.portal.bid.entity.Form;
import com.portal.bid.repository.ApprovalNotificationsRepository;
import com.portal.bid.repository.ApprovalRequestRepository;
import com.portal.bid.repository.FormRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApprovalNotificationsService {

    @Autowired
    private ApprovalNotificationsRepository repository;

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;

    @Autowired
    private  FormRepository formRepository;


    public List<ApprovalNotificationDTO> getApprovalsByUserId(Long userId) {
        List<ApprovalNotifications> approvals = repository.findByUserId(userId);

        return approvals.stream().map(approval -> {
            ApprovalNotificationDTO dto = new ApprovalNotificationDTO(approval, "");

            // Set all the fields from ApprovalNotifications
            dto.setNotificationId(approval.getId());
            dto.setUserId(approval.getUserId());
            dto.setRemarks(approval.getRemarks());
            dto.setReadStatus(approval.getIsActionTaken());
            dto.setCreatedAt(approval.getCreatedAt());
            dto.setAction(approval.getAction());

            // Fetch and set the PPT content
            Optional<ApprovalRequest> approvalRequest = approvalRequestRepository.findById(approval.getApprovalId());
            if (approvalRequest.isPresent()) {
                String pptFilePath = approvalRequest.get().getPptFilePath();
                try {
                    byte[] fileContent = Files.readAllBytes(Paths.get(pptFilePath));
                    dto.setPptFileContent(fileContent);
                } catch (IOException e) {
                    // Handle the exception (log it, set error message in DTO, etc.)
                    e.printStackTrace();
                    dto.setPptFileContent(null); // or set an error message
                }

                // Set additional fields from ApprovalRequest if needed
                dto.setApprovalRequestId(approvalRequest.get().getId());
                // Add any other fields you want to include from ApprovalRequest
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public Optional<ApprovalNotifications> getApprovalById(Long id) {
        return repository.findById(id);
    }

    public ApprovalNotifications createApproval(ApprovalNotifications approval) {
        return repository.save(approval);
    }

    @Transactional
    public ApprovalNotifications updateApproval(Long id, ApprovalNotifications approvalDetails) {
        Optional<ApprovalNotifications> approval = repository.findById(id);

        if (approval.isPresent()) {
            ApprovalNotifications existingApproval = approval.get();
            existingApproval.setRemarks(approvalDetails.getRemarks());
            existingApproval.setAction(approvalDetails.getAction());
            existingApproval.setIsActionTaken(approvalDetails.getIsActionTaken());

            ApprovalNotifications updatedApproval = repository.save(existingApproval);

            // Check if all related notifications have isActionTaken set to true
            Long approvalId = existingApproval.getApprovalId();
            List<ApprovalNotifications> relatedNotifications = repository.findByApprovalId(approvalId);

            boolean allActioned = relatedNotifications.stream()
                    .allMatch(ApprovalNotifications::getIsActionTaken);

            Optional<ApprovalRequest> approvalRequest = approvalRequestRepository.findById(approvalId);

            if (allActioned) {
                // Update ApprovalRequest's actionPerformed to "yes"
                approvalRequest.ifPresent(request -> {
                    request.setActionPerformed("yes");
                    approvalRequestRepository.save(request);
                });

                // Fetch the associated form and update the GoNoGoStatus
                approvalRequest.ifPresent(request -> {
                    Optional<Form> form = formRepository.findById(request.getFormId());

                    form.ifPresent(f -> {
                        // Determine GoNoGoStatus: Approved if all are 'true', else Rejected
                        boolean allApproved = relatedNotifications.stream()
                                .allMatch(notification -> Boolean.TRUE.equals(notification.getAction()));

                        f.setGoNoGoStatus(allApproved ? "Approved" : "Rejected");
                        formRepository.save(f);
                    });
                });
            } else {
                // If not all notifications are actioned, mark the form status as Pending
                approvalRequest.ifPresent(request -> {
                    Optional<Form> form = formRepository.findById(request.getFormId());

                    form.ifPresent(f -> {
                        f.setGoNoGoStatus("Pending");
                        formRepository.save(f);
                    });
                });
            }

            return updatedApproval;
        }
        return null;
    }

}