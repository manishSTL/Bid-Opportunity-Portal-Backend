package com.portal.bid.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "opportunity")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("priority_bid")
    @Column(name = "priority_bid")
//    @Pattern(regexp = "^(Yes|No)$", message = "Priority bid must be either 'Yes' or 'No'")
    private String priorityBid;

    @JsonProperty("ob_fy")
    @NotBlank(message = "Financial year is required")
//    @Pattern(regexp = "\\d{4}-\\d{4}", message = "Financial year must be in format YYYY-YYYY")
    @Column(name = "ob_fy", nullable = false)
    private String obFy;

    @JsonProperty("ob_qtr")
    @NotBlank(message = "Quarter is required")
    @Pattern(regexp = "Q[1-4]", message = "Quarter must be in format Q1, Q2, Q3, or Q4")
    @Column(name = "ob_qtr", nullable = false)
    private String obQtr;

    @JsonProperty("ob_mmm")
    @NotBlank(message = "Month is required")
//    @Pattern(regexp = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)$", message = "Month must be in MMM format")
    @Column(name = "ob_mmm", nullable = false)
    private String obMmm;

    @JsonProperty("priority")
    @NotBlank(message = "Priority is required")
//    @Pattern(regexp = "^(High|Medium|Low)$", message = "Priority must be High, Medium, or Low")
    @Column(name = "priority", nullable = false)
    private String priority;

    @JsonProperty("opportunity")
    @NotBlank(message = "Opportunity name is required")
//    @Size(min = 3, max = 30, message = "Permission name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Opportunity can only contain letters, numbers, dots, hyphens, and underscores")
    @Column(name = "opportunity", nullable = false)
    private String opportunity;

    @JsonProperty("opportunity_type")
//    @NotBlank(message = "Opportunity type is required")
    @Column(name = "opportunity_type", nullable = false)
    private String opportunityType;

    @JsonProperty("amount_inr_cr_max")
    @NotNull(message = "Maximum amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum amount must be greater than or equal to 0")
    @Column(name = "amount_inr_cr_max", nullable = false)
    private BigDecimal amountInrCrMax;

    @JsonProperty("amount_inr_cr_min")
    @NotNull(message = "Minimum amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum amount must be greater than or equal to 0")
    @Column(name = "amount_inr_cr_min", nullable = false)
    private BigDecimal amountInrCrMin;

    @JsonProperty("rev_in_ob_qtr")
    @DecimalMin(value = "0.0", inclusive = true, message = "Revenue must be greater than or equal to 0")
    @Column(name = "rev_in_ob_qtr")
    private BigDecimal revInObQtr;

    @JsonProperty("rev_in_ob_qtr_plus_1")
    @DecimalMin(value = "0.0", inclusive = true, message = "Revenue must be greater than or equal to 0")
    @Column(name = "rev_in_ob_qtr_plus_1")
    private BigDecimal revInObQtrPlus1;

    @JsonProperty("business_unit")
    @NotBlank(message = "Business unit is required")
    @Column(name = "business_unit", nullable = false)
    private String businessUnit;

    @JsonProperty("industry_segment")
    @NotBlank(message = "Industry segment is required")
    @Column(name = "industry_segment", nullable = false)
    private String industrySegment;

    @JsonProperty("primary_offering_segment")
    @Length(max = 255)
    @Column(name = "primary_offering_segment")
    private String primaryOfferingSegment;

    @JsonProperty("secondary_offering_segment")
    @Length(max = 255)
    @Column(name = "secondary_offering_segment")
    private String secondaryOfferingSegment;

    @JsonProperty("part_quarter")
//    @NotBlank(message = "Part quarter is required")
//    @Pattern(regexp = "Q[1-4]", message = "Part quarter must be in format Q1, Q2, Q3, or Q4")
    @Column(name = "part_quarter", nullable = false)
    private String partQuarter;

    @JsonProperty("part_month")
//    @NotBlank(message = "Part month is required")
//    @Pattern(regexp = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)$", message = "Part month must be in MMM format")
    @Column(name = "part_month", nullable = false)
    private String partMonth;

    @JsonProperty("project_tenure_months")
    @Min(value = 1, message = "Project tenure must be at least 1 month")
    @Max(value = 120, message = "Project tenure cannot exceed 120 months")
    @Column(name = "project_tenure_months")
    private Integer projectTenureMonths;

    @JsonProperty("est_capex_inr_cr")
    @DecimalMin(value = "0.0", inclusive = true, message = "Estimated CAPEX must be greater than or equal to 0")
    @Column(name = "est_capex_inr_cr")
    private BigDecimal estCapexInrCr;

    @JsonProperty("est_opex_inr_cr")
    @DecimalMin(value = "0.0", inclusive = true, message = "Estimated OPEX must be greater than or equal to 0")
    @Column(name = "est_opex_inr_cr")
    private BigDecimal estOpexInrCr;

    @JsonProperty("opex_tenure_months")
    @Min(value = 0, message = "OPEX tenure cannot be negative")
    @Max(value = 120, message = "OPEX tenure cannot exceed 120 months")
    @Column(name = "opex_tenure_months")
    private Integer opexTenureMonths;

    @JsonProperty("deal_status")
    @NotBlank(message = "Deal status is required")
    @Column(name = "deal_status", nullable = false)
    private String dealStatus;

    @JsonProperty("go_no_go_status")
//    @Pattern(regexp = "^(Go|No Go|Pending)?$", message = "Go/No Go status must be Go, No Go, or Pending")
    @Column(name = "go_no_go_status")
    private String goNoGoStatus;

    @JsonProperty("go_no_go_date")
    @Column(name = "go_no_go_date")
    private LocalDate goNoGoDate;

    @JsonProperty("solution_readiness")
//    @Pattern(regexp = "^(Ready|Not Ready|In Progress)?$", message = "Solution readiness must be Ready, Not Ready, or In Progress")
    @Column(name = "solution_readiness")
    private String solutionReadiness;

    @JsonProperty("customer_alignment")
    @Length(max = 255)
    @Column(name = "customer_alignment")
    private String customerAlignment;

    @JsonProperty("stl_preparedness")
    @Length(max = 255)
    @Column(name = "stl_preparedness")
    private String stlPreparedness;

    @JsonProperty("readiness_as_per_timeline")
    @Length(max = 255)
    @Column(name = "readiness_as_per_timeline")
    private String readinessAsPerTimeline;

    @JsonProperty("gm_percentage")
//    @Pattern(regexp = "^\\d+(\\.\\d+)?%?$", message = "GM percentage must be a number or percentage (e.g., 50 or 50.5%)")
    @Column(name = "gm_percentage")
    private String gmPercentage;

    @JsonProperty("probability")
//    @Pattern(regexp = "^\\d+(\\.\\d+)?%?$", message = "Probability must be a number or percentage (e.g., 50 or 50.5%)")
    @Column(name = "probability")
    private String probability;

    @JsonProperty("sales_role")
    @NotBlank(message = "Sales role is required")
    @Size(min = 3, max = 50, message = "Sales role must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Sales role can only contain letters, numbers, dots, hyphens, and underscores")
    @Column(name = "sales_role", nullable = false)
    private String salesRole;

    @JsonProperty("primary_owner")
    @NotBlank(message = "Primary owner is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Primary owner must contain only alphabetic characters and spaces")
    @Column(name = "primary_owner", nullable = false)
    private String primaryOwner;

    @JsonProperty("leader_for_aircover")
//    @Pattern(regexp = "^[A-Za-z ]+$", message = "Leader for aircover must contain only alphabetic characters and spaces")
    @Column(name = "leader_for_aircover")
    private String leaderForAircover;

    @JsonProperty("source")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Source must contain only alphabetic characters and spaces")
    @Column(name = "source")
    private String source;

    @JsonProperty("source_person")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Source person must contain only alphabetic characters and spaces")
    @Column(name = "source_person")
    private String sourcePerson;

    @JsonProperty("lead_received_date")
    @PastOrPresent(message = "Lead received date cannot be in the future")
    @Column(name = "lead_received_date")
    private LocalDate leadReceivedDate;

    @JsonProperty("release_date")
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @JsonProperty("submission_date")
    @NotNull(message = "Submission date is required")
    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @JsonProperty("decision_date")
    @Column(name = "decision_date")
    private LocalDate decisionDate;

    @JsonProperty("additional_remarks")
//    @Size(min = 3, max = 50, message = "Permission name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Additional remarks can only contain letters, numbers, dots, hyphens, and underscores")
    @Column(name = "additional_remarks")
    private String additionalRemarks;

    @JsonProperty("tender_no")
    @Length(max = 50, message = "Tender number cannot exceed 50 characters")
    @Column(name = "tender_no")
    private String tenderNo;

    @JsonProperty("scope_of_work")
    @Length(max = 2000, message = "Scope of work cannot exceed 2000 characters")
    @Column(name = "scope_of_work", columnDefinition = "TEXT")
    private String scopeOfWork;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    @Length(max = 255)
    @Column(name = "created_by")
    private String createdBy;

    @JsonProperty("business_services")
    @Length(max = 255)
    @Column(name = "business_services")
    private String businessService;

    @JsonProperty("est_capex_phase")
    @Min(value = 0, message = "Estimated CAPEX phase cannot be negative")
    @Column(name = "est_capex_phase")
    private Integer estCapexPhase;

    @JsonProperty("est_opex_phase")
    @Min(value = 0, message = "Estimated OPEX phase cannot be negative")
    @Column(name = "est_opex_phase")
    private Integer estOpexPhase;

    @JsonProperty("customer_name")
    @Length(max = 255, message = "Customer name cannot exceed 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Customer name can only contain letters, numbers, dots, hyphens, and underscores")
    @Column(name = "customer_name")
    private String customerName;

    @JsonProperty("logo")
    @Length(max = 255)
    @Column(name = "logo")
    private String logo;

    @JsonProperty("updated_by")
    @Length(max = 255)
    @Column(name = "updated_by")
    private String updatedBy;

    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPriorityBid() {
        return priorityBid;
    }

    public void setPriorityBid(String priorityBid) {
        this.priorityBid = priorityBid;
    }

    public String getObFy() {
        return obFy;
    }

    public void setObFy(String obFy) {
        this.obFy = obFy;
    }

    public String getObQtr() {
        return obQtr;
    }

    public void setObQtr(String obQtr) {
        this.obQtr = obQtr;
    }

    public String getObMmm() {
        return obMmm;
    }

    public void setObMmm(String obMmm) {
        this.obMmm = obMmm;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(String opportunity) {
        this.opportunity = opportunity;
    }

    public String getOpportunityType() {
        return opportunityType;
    }

    public void setOpportunityType(String opportunityType) {
        this.opportunityType = opportunityType;
    }

    public BigDecimal getAmountInrCrMax() {
        return amountInrCrMax;
    }

    public void setAmountInrCrMax(BigDecimal amountInrCrMax) {
        this.amountInrCrMax = amountInrCrMax;
    }

    public BigDecimal getAmountInrCrMin() {
        return amountInrCrMin;
    }

    public void setAmountInrCrMin(BigDecimal amountInrCrMin) {
        this.amountInrCrMin = amountInrCrMin;
    }

    public BigDecimal getRevInObQtr() {
        return revInObQtr;
    }

    public void setRevInObQtr(BigDecimal revInObQtr) {
        this.revInObQtr = revInObQtr;
    }

    public BigDecimal getRevInObQtrPlus1() {
        return revInObQtrPlus1;
    }

    public void setRevInObQtrPlus1(BigDecimal revInObQtrPlus1) {
        this.revInObQtrPlus1 = revInObQtrPlus1;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getIndustrySegment() {
        return industrySegment;
    }

    public void setIndustrySegment(String industrySegment) {
        this.industrySegment = industrySegment;
    }

    public String getPrimaryOfferingSegment() {
        return primaryOfferingSegment;
    }

    public void setPrimaryOfferingSegment(String primaryOfferingSegment) {
        this.primaryOfferingSegment = primaryOfferingSegment;
    }

    public String getSecondaryOfferingSegment() {
        return secondaryOfferingSegment;
    }

    public void setSecondaryOfferingSegment(String secondaryOfferingSegment) {
        this.secondaryOfferingSegment = secondaryOfferingSegment;
    }

    public String getPartQuarter() {
        return partQuarter;
    }

    public void setPartQuarter(String partQuarter) {
        this.partQuarter = partQuarter;
    }

    public String getPartMonth() {
        return partMonth;
    }

    public void setPartMonth(String partMonth) {
        this.partMonth = partMonth;
    }

    public Integer getProjectTenureMonths() {
        return projectTenureMonths;
    }

    public void setProjectTenureMonths(Integer projectTenureMonths) {
        this.projectTenureMonths = projectTenureMonths;
    }

    public BigDecimal getEstCapexInrCr() {
        return estCapexInrCr;
    }

    public void setEstCapexInrCr(BigDecimal estCapexInrCr) {
        this.estCapexInrCr = estCapexInrCr;
    }

    public BigDecimal getEstOpexInrCr() {
        return estOpexInrCr;
    }

    public void setEstOpexInrCr(BigDecimal estOpexInrCr) {
        this.estOpexInrCr = estOpexInrCr;
    }

    public Integer getOpexTenureMonths() {
        return opexTenureMonths;
    }

    public void setOpexTenureMonths(Integer opexTenureMonths) {
        this.opexTenureMonths = opexTenureMonths;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getGoNoGoStatus() {
        return goNoGoStatus;
    }

    public void setGoNoGoStatus(String goNoGoStatus) {
        this.goNoGoStatus = goNoGoStatus;
    }

    public LocalDate getGoNoGoDate() {
        return goNoGoDate;
    }

    public void setGoNoGoDate(LocalDate goNoGoDate) {
        this.goNoGoDate = goNoGoDate;
    }

    public String getSolutionReadiness() {
        return solutionReadiness;
    }

    public void setSolutionReadiness(String solutionReadiness) {
        this.solutionReadiness = solutionReadiness;
    }

    public String getCustomerAlignment() {
        return customerAlignment;
    }

    public void setCustomerAlignment(String customerAlignment) {
        this.customerAlignment = customerAlignment;
    }

    public String getStlPreparedness() {
        return stlPreparedness;
    }

    public void setStlPreparedness(String stlPreparedness) {
        this.stlPreparedness = stlPreparedness;
    }

    public String getReadinessAsPerTimeline() {
        return readinessAsPerTimeline;
    }

    public void setReadinessAsPerTimeline(String readinessAsPerTimeline) {
        this.readinessAsPerTimeline = readinessAsPerTimeline;
    }

    public String getGmPercentage() {
        return gmPercentage;
    }

    public void setGmPercentage(String gmPercentage) {
        this.gmPercentage = gmPercentage;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public String getSalesRole() {
        return salesRole;
    }

    public void setSalesRole(String salesRole) {
        this.salesRole = salesRole;
    }

    public String getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(String primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public String getLeaderForAircover() {
        return leaderForAircover;
    }

    public void setLeaderForAircover(String leaderForAircover) {
        this.leaderForAircover = leaderForAircover;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourcePerson() {
        return sourcePerson;
    }

    public void setSourcePerson(String sourcePerson) {
        this.sourcePerson = sourcePerson;
    }

    public LocalDate getLeadReceivedDate() {
        return leadReceivedDate;
    }

    public void setLeadReceivedDate(LocalDate leadReceivedDate) {
        this.leadReceivedDate = leadReceivedDate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDate getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDate decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getAdditionalRemarks() {
        return additionalRemarks;
    }

    public void setAdditionalRemarks(String additionalRemarks) {
        this.additionalRemarks = additionalRemarks;
    }

    public String getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(String tenderNo) {
        this.tenderNo = tenderNo;
    }

    public String getScopeOfWork() {
        return scopeOfWork;
    }

    public void setScopeOfWork(String scopeOfWork) {
        this.scopeOfWork = scopeOfWork;
    }

    // Getter and Setter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getter and Setter for createdBy
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // Getter and Setter for businessService
    public String getBusinessService() {
        return businessService;
    }

    public void setBusinessService(String businessService) {
        this.businessService = businessService;
    }

    public Integer getEstCapexPhase() {
        return estCapexPhase;
    }

    public void setEstCapexPhase(Integer estCapexPhase) {
        this.estCapexPhase = estCapexPhase;
    }

    // Getter and Setter for estOpexPhase
    public Integer getEstOpexPhase() {
        return estOpexPhase;
    }

    public void setEstOpexPhase(Integer estOpexPhase) {
        this.estOpexPhase = estOpexPhase;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    // Getter and Setter for updatedBy
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Getter and Setter for updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public  String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}