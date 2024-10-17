package com.portal.bid.dto;

import com.portal.bid.entity.ApprovalNotifications;

import java.time.LocalDateTime;
import java.util.Base64;

public class ApprovalNotificationDTO {
    private Long notificationId;
    private Long userId;
    private String remarks;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Boolean action;
    private String pptFileContent;
    private Long approvalRequestId;
    private String userName;  // Fixed: replaced `:` with `;`

    // Constructor using entity and additional parameters
    public ApprovalNotificationDTO(ApprovalNotifications notification, String userName) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }
        this.notificationId = notification.getId();
        this.userId = notification.getUserId();
        this.userName = userName;
        this.remarks = notification.getRemarks();
        this.isRead = notification.getIsActionTaken();
        this.createdAt = notification.getCreatedAt();
        this.action = notification.getAction();
    }

    // Getters and Setters for all fields
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
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

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public String getPptFileContent() {
        return pptFileContent;
    }

    public void setPptFileContent(byte[] pptFileContent) {
        this.pptFileContent = pptFileContent != null ?
                Base64.getEncoder().encodeToString(pptFileContent) : null;
    }

    public Long getApprovalRequestId() {
        return approvalRequestId;
    }

    public void setApprovalRequestId(Long approvalRequestId) {
        this.approvalRequestId = approvalRequestId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ApprovalNotificationDTO{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", remarks='" + remarks + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                ", action=" + action +
                ", pptFileContent='" +
                (pptFileContent != null ? "[CONTENT AVAILABLE]" : "[CONTENT NOT AVAILABLE]") + '\'' +
                ", approvalRequestId=" + approvalRequestId +
                '}';
    }

    public void setReadStatus(Boolean isActionTaken) {
        this.isRead=isActionTaken;
    }
}
