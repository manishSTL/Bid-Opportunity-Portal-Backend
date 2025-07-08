package com.portal.bid.dto;

public class ApprovalFormInfoDTO {
    private Long approvalRequestId;
    private String formName;

    public ApprovalFormInfoDTO(Long approvalRequestId, String formName) {
        this.approvalRequestId = approvalRequestId;
        this.formName = formName;
    }

    // Getters and setters
    public Long getApprovalRequestId() {
        return approvalRequestId;
    }

    public void setApprovalRequestId(Long approvalRequestId) {
        this.approvalRequestId = approvalRequestId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}