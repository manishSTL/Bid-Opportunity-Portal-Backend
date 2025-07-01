package com.portal.bid.entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank(message = "Opportunity name is required")
    @Size(max = 300, message = "Opportunity name must be less than 100 characters")
    @Column(name = "opportunity_name", nullable = true, length = 300)
    private String opportunityName;

    // @NotNull(message = "Part fiscal year is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_fy")
    private Fy partFy;

    // @NotBlank(message = "Part quarter is required")
    @Pattern(regexp = "^Q[1-4]$", message = "Part quarter must be in format Q1, Q2, Q3, or Q4")
    @Column(name = "part_quarter", nullable = true, length = 10)
    private String partQuarter;

    // @NotBlank(message = "Part month is required")
    @Pattern(regexp = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)$", 
             message = "Part month must be a valid 3-letter month abbreviation")
    @Column(name = "part_month", nullable = true, length = 10)
    private String partMonth;

    // @NotNull(message = "OB fiscal year is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ob_fy")
    private Fy obFy;

    // @NotBlank(message = "OB quarter is required")
    @Pattern(regexp = "^Q[1-4]$", message = "OB quarter must be in format Q1, Q2, Q3, or Q4")
    @Column(name = "ob_qtr", nullable = true, length = 3)
    private String obQtr;

    // @NotBlank(message = "OB month is required")
    @Pattern(regexp = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)$", 
             message = "OB month must be a valid 3-letter month abbreviation")
    @Column(name = "ob_mmm", nullable = true, length = 10)
    private String obMmm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority")
    private Priority priority;

    // @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Amount must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "amount", nullable = true, precision = 15, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_status")
    private Deal dealStatus;

    @PositiveOrZero(message = "Actual booked OB must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Actual booked OB must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "actual_booked_ob", precision = 15, scale = 2)
    private BigDecimal actualBookedOb;

    @PositiveOrZero(message = "Actual booked CAPEX must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Actual booked CAPEX must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "actual_booked_capex", precision = 15, scale = 2)
    private BigDecimal actualBookedCapex;

    @PositiveOrZero(message = "Actual booked OPEX must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Actual booked OPEX must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "actual_booked_opex", precision = 15, scale = 2)
    private BigDecimal actualBookedOpex;

    @PositiveOrZero(message = "Revenue in OB quarter must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Revenue in OB quarter must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "rev_in_ob_qtr", precision = 15, scale = 2)
    private BigDecimal revInObQtr;

    @PositiveOrZero(message = "Revenue in OB quarter plus 1 must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Revenue in OB quarter plus 1 must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "rev_in_ob_qtr_plus_1", precision = 15, scale = 2)
    private BigDecimal revInObQtrPlus1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_segment")
    private BusinessSegment industrySegment;

    @Pattern(regexp = "^(Public|Private)$", message = "Public/Private must be either 'Public' or 'Private'")
    @Column(name = "public_private", length = 30)
    private String publicPrivate;

    @Size(max = 50, message = "Primary offering segment must be less than 50 characters")
    @Column(name = "primary_offering_segment", length = 50)
    private String primaryOfferingSegment;

    @Size(max = 50, message = "Secondary offering segment must be less than 50 characters")
    @Column(name = "secondary_offering_segment", length = 50)
    private String secondaryOfferingSegment;

    @Min(value = 1, message = "Project tenure must be at least 1 month")
    @Max(value = 120, message = "Project tenure cannot exceed 120 months (10 years)")
    @Column(name = "project_tenure_months")
    private Integer projectTenureMonths;

    @PositiveOrZero(message = "Estimated CAPEX must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Estimated CAPEX must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "est_capex_inr_cr", precision = 15, scale = 2)
    private BigDecimal estCapexInrCr;

    @PositiveOrZero(message = "Estimated OPEX must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Estimated OPEX must have at most 13 digits in integer part and 2 digits in fraction part")
    @Column(name = "est_opex_inr_cr", precision = 15, scale = 2)
    private BigDecimal estOpexInrCr;

    @Min(value = 0, message = "OPEX tenure must be at least 0 months")
    @Max(value = 120, message = "OPEX tenure cannot exceed 120 months (10 years)")
    @Column(name = "opex_tenure_months")
    private Integer opexTenureMonths;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "go_no_go_status")
    private GoNoGoMaster goNoGoMaster;

    @Column(name = "go_no_go_date")
    private LocalDate goNoGoDate;

    @DecimalMin(value = "0.0", message = "GM percentage must be at least 0")
    @DecimalMax(value = "100.0", message = "GM percentage cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "GM percentage must have at most 3 digits in integer part and 2 digits in fraction part")
    @Column(name = "gm_percentage", precision = 5, scale = 2)
    private BigDecimal gmPercentage;

    @Min(value = 0, message = "Probability must be at least 0")
    @Max(value = 100, message = "Probability cannot exceed 100")
    @Column(name = "probability")
    private Integer probability;

    // @NotBlank(message = "Primary owner is required")
    @Size(max = 100, message = "Primary owner must be less than 100 characters")
    @Column(name = "primary_owner", nullable = true, length = 100)
    private String primaryOwner;

    @Size(max = 100, message = "Solution SPOC must be less than 100 characters")
    @Column(name = "solution_spoc", length = 100)
    private String solutionSpoc;

    @Size(max = 100, message = "SCM SPOC must be less than 100 characters")
    @Column(name = "scm_spoc", length = 100)
    private String scmSpoc;

    @Size(max = 200, message = "Remarks must be less than 100 characters")
    @Column(name = "remarks", length = 100)
    private String remarks;

    @Size(max = 200, message = "PQ/TQ remarks must be less than 100 characters")
    @Column(name = "pqTq_remarks", length = 100)
    private String pqTq_remarks;

    @Column(name = "rfp_release_date")
    private LocalDate rfpReleaseDate;

    // @Future(message = "Bid submission date must be in the future")
    @Column(name = "bid_submission_date")
    private LocalDate bidSubmissionDate;

    @NotNull(message = "Created by is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;
}