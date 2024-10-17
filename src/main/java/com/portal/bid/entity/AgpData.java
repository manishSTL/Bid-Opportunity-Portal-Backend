package com.portal.bid.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agp_data")
public class AgpData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "business_segment_id")
    private BusinessSegment businessSegment;

    private String financialYear;
    private String quarter;
    private BigDecimal agpValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public BusinessSegment getBusinessSegment() {
        return businessSegment;
    }

    public void setBusinessSegment(BusinessSegment businessSegment) {
        this.businessSegment = businessSegment;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getAgpValue() {
        return agpValue;
    }

    public void setAgpValue(BigDecimal agpValue) {
        this.agpValue = agpValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AgpData{" +
                "id=" + id +
                ", user=" + user +
                ", department=" + department +
                ", businessSegment=" + businessSegment +
                ", financialYear='" + financialYear + '\'' +
                ", quarter='" + quarter + '\'' +
                ", agpValue=" + agpValue +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
