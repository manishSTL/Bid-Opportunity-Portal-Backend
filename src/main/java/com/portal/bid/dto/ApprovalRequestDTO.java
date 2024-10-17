package com.portal.bid.dto;

import com.portal.bid.entity.ApprovalRequest;

import java.time.LocalDateTime;

public class ApprovalRequestDTO {
    private Long id;
    private String pptFilePath;
    private Long formId;
    private LocalDateTime createdAt;
    private String createdBy;
    private String actionPerformed;

    // Constructor
    public ApprovalRequestDTO(ApprovalRequest approvalRequest) {
        this.id = approvalRequest.getId();
        this.pptFilePath = approvalRequest.getPptFilePath();
        this.formId = approvalRequest.getFormId();
        this.createdAt = approvalRequest.getCreatedAt();
        this.createdBy = approvalRequest.getCreatedBy();
        this.actionPerformed = approvalRequest.getActionPerformed();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPptFilePath() {
        return pptFilePath;
    }

    public void setPptFilePath(String pptFilePath) {
        this.pptFilePath = pptFilePath;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }
}
