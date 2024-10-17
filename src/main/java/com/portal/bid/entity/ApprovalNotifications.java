package com.portal.bid.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_notifications")
public class ApprovalNotifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approval_id", nullable = false)
    private Long approvalId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "action")
    private Boolean action;

    @Column(name = "is_action_taken")
    private Boolean isActionTaken;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public ApprovalNotifications() {
    }

    public ApprovalNotifications(Long approvalId, Long userId) {
        this.approvalId = approvalId;
        this.userId = userId;
        this.isActionTaken = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public Boolean getIsActionTaken() {
        return isActionTaken;
    }

    public void setIsActionTaken(Boolean isActionTaken) {
        this.isActionTaken = isActionTaken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}