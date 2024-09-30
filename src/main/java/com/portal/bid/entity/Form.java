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

@Entity
@Table(name = "opportunity")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("priority_bid")
    @Column(name = "priority_bid")
    private String priorityBid;

    @JsonProperty("ob_fy")
    @Column(name = "ob_fy",nullable = false)
    private String obFy;

    @JsonProperty("ob_qtr")
    @Column(name = "ob_qtr",nullable = false)
    private String obQtr;

    @JsonProperty("ob_mmm")
    @Column(name = "ob_mmm",nullable = false)
    private String obMmm;

    @JsonProperty("priority")
    @Column(name = "priority",nullable = false)
    private String priority;

    @JsonProperty("opportunity")
    @Column(name = "opportunity",nullable = false)
    private String opportunity;

    @JsonProperty("opportunity_type")
    @Column(name = "opportunity_type",nullable = false)
    private String opportunityType;

    @JsonProperty("amount_inr_cr_max")
    @Column(name = "amount_inr_cr_max",nullable = false)
    private BigDecimal amountInrCrMax;

    @JsonProperty("amount_inr_cr_min")
    @Column(name = "amount_inr_cr_min",nullable = false)
    private BigDecimal amountInrCrMin;

    @JsonProperty("rev_in_ob_qtr")
    @Column(name = "rev_in_ob_qtr")
    private BigDecimal revInObQtr;

    @JsonProperty("rev_in_ob_qtr_plus_1")
    @Column(name = "rev_in_ob_qtr_plus_1")
    private BigDecimal revInObQtrPlus1;

    @JsonProperty("business_unit")
    @Column(name = "business_unit",nullable = false)
    private String businessUnit;

    @JsonProperty("industry_segment")
    @Column(name = "industry_segment",nullable = false)
    private String industrySegment;

    @JsonProperty("primary_offering_segment")
    @Column(name = "primary_offering_segment")
    private String primaryOfferingSegment;

    @JsonProperty("secondary_offering_segment")
    @Column(name = "secondary_offering_segment")
    private String secondaryOfferingSegment;

    @JsonProperty("part_quarter")
    @Column(name = "part_quarter",nullable = false)
    private String partQuarter;

    @JsonProperty("part_month")
    @Column(name = "part_month",nullable = false)
    private String partMonth;

    @JsonProperty("project_tenure_months")
    @Column(name = "project_tenure_months")
    private Integer projectTenureMonths;

    @JsonProperty("est_capex_inr_cr")
    @Column(name = "est_capex_inr_cr")
    private BigDecimal estCapexInrCr;

    @JsonProperty("est_opex_inr_cr")
    @Column(name = "est_opex_inr_cr")
    private BigDecimal estOpexInrCr;

    @JsonProperty("opex_tenure_months")
    @Column(name = "opex_tenure_months")
    private Integer opexTenureMonths;

    @JsonProperty("deal_status")
    @Column(name = "deal_status",nullable = false)
    private String dealStatus;

    @JsonProperty("go_no_go_status")
    @Column(name = "go_no_go_status")
    private String goNoGoStatus;

    @JsonProperty("go_no_go_date")
    @Column(name = "go_no_go_date")
    private LocalDate goNoGoDate;

    @JsonProperty("solution_readiness")
    @Column(name = "solution_readiness")
    private String solutionReadiness;

    @JsonProperty("customer_alignment")
    @Column(name = "customer_alignment")
    private String customerAlignment;

    @JsonProperty("stl_preparedness")
    @Column(name = "stl_preparedness")
    private String stlPreparedness;

    @JsonProperty("readiness_as_per_timeline")
    @Column(name = "readiness_as_per_timeline")
    private String readinessAsPerTimeline;

    @JsonProperty("gm_percentage")
    @Column(name = "gm_percentage")
//    @Pattern(regexp = "^\\d+(\\.\\d+)?%?$", message = "gmPercentage must be a number or percentage (e.g., 50 or 50.5%)")
    private String gmPercentage;

    @JsonProperty("probability")
    @Column(name = "probability")
//    @Pattern(regexp = "^\\d+(\\.\\d+)?%?$", message = "Probability must be a number or percentage (e.g., 50 or 50.5%)")
    private String probability;

    @JsonProperty("sales_role")
    @Column(name = "sales_role",nullable = false)
    private String salesRole;

    @JsonProperty("primary_owner")
    @Column(name = "primary_owner",nullable = false)
//    @Pattern(regexp = "^[A-Za-z]+$", message = "primary Owner must contain only alphabetic characters")
    private String primaryOwner;

    @JsonProperty("leader_for_aircover")
    @Column(name = "leader_for_aircover")
//    @Pattern(regexp = "^[A-Za-z]+$", message = "Leader for aircover must contain only alphabetic characters")
    private String leaderForAircover;

    @JsonProperty("source")
    @Column(name = "source")
//    @Pattern(regexp = "^[A-Za-z]+$", message = "Source must contain only alphabetic characters")
    private String source;

    @JsonProperty("source_person")
    @Column(name = "source_person")
//    @Pattern(regexp = "^[A-Za-z]+$", message = "Source person must contain only alphabetic characters")
    private String sourcePerson;

    @JsonProperty("lead_received_date")
    @Column(name = "lead_received_date")
    private LocalDate leadReceivedDate;

    @JsonProperty("release_date")
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @JsonProperty("submission_date")
    @Column(name = "submission_date",nullable = false)
    private LocalDate submissionDate;

    @JsonProperty("decision_date")
    @Column(name = "decision_date")
    private LocalDate decisionDate;

    @JsonProperty("additional_remarks")
    @Column(name = "additional_remarks")
    private String additionalRemarks;

    @JsonProperty("tender_no")
    @Column(name = "tender_no")
    private String tenderNo;

    @JsonProperty("scope_of_work")
    @Column(name = "scope_of_work", columnDefinition = "TEXT")
    private String scopeOfWork;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    @Column(name = "created_by")
    private String createdBy;

    @JsonProperty("business_services")
    @Column(name = "business_services")
    private String businessService;

    @JsonProperty("est_capex_phase")
    @Column(name = "est_capex_phase")
    private Integer estCapexPhase;

    @JsonProperty("est_opex_phase")
    @Column(name = "est_opex_phase")
    private Integer estOpexPhase;

    @JsonProperty("customer_name")
    @Column(name = "customer_name")
    private String customerName;

    @JsonProperty("logo")
    @Column(name = "logo")
    private String logo;

    @JsonProperty("updated_by")
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