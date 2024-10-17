package com.portal.bid.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "approval_request")
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ppt_file_path", nullable = false)
    private String pptFilePath;

    @Column(name = "form_id", nullable = false)
    private Long formId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "action_performed", nullable = false)
    private String actionPerformed = "no";

    // Constructors

    public ApprovalRequest() {
    }

    public ApprovalRequest(String pptFilePath, Long formId, String createdBy) {
        this.pptFilePath = pptFilePath;
        this.formId = formId;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "ApprovalRequest{" +
                "id=" + id +
                ", pptFilePath='" + pptFilePath + '\'' +
                ", formId=" + formId +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", actionPerformed='" + actionPerformed + '\'' +
                '}';
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