package com.portal.bid.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agp_data")
public class AgpData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Department is required")
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @NotNull(message = "Business segment is required")
    @ManyToOne
    @JoinColumn(name = "business_segment_id")
    private BusinessSegment businessSegment;

    @NotBlank(message = "Financial year is required")
//    @Pattern(regexp = "\\d{4}-\\d{4}", message = "Financial year must be in format YYYY-YYYY")
    @Column(nullable = false)
    private String financialYear;

    @NotBlank(message = "Quarter is required")
    @Pattern(regexp = "Q[1-4]", message = "Quarter must be in format Q1, Q2, Q3, or Q4")
    @Column(nullable = false)
    private String quarter;

//    @NotNull(message = "AGP value is required")
//    @DecimalMin(value = "0.0", inclusive = true, message = "AGP value must be greater than or equal to 0")
    @Digits(integer = 12, fraction = 2, message = "AGP value must have at most 12 digits in total and 2 decimal places")
    @Column(nullable = false)
    private BigDecimal agpValue;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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