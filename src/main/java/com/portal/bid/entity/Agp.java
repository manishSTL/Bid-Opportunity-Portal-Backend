package com.portal.bid.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "agp")
public class Agp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private long employeeID;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "ob_fy", nullable = false)
    private String obFY;

    @Column(name = "ob_qt", nullable = false)
    private String obQT;

    @Column(name = "agp_value", nullable = false)
    private int agpValue;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt =LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt =LocalDateTime.now();
    }


    public enum Status {
        ACTIVE, INACTIVE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getObFY() {
        return obFY;
    }

    public void setObFY(String obFY) {
        this.obFY = obFY;
    }

    public String getObQT() {
        return obQT;
    }

    public void setObQT(String obQT) {
        this.obQT = obQT;
    }

    public int getAgpValue() {
        return agpValue;
    }

    public void setAgpValue(int agpValue) {
        this.agpValue = agpValue;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
