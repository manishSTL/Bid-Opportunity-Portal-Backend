package com.portal.bid.controller;

import java.time.LocalDate;
import java.util.List;

public class DuplicateOpportunityResponse {
    private String message;
    private List<PotentialDuplicate> potentialDuplicates;

    public DuplicateOpportunityResponse(String message, List<PotentialDuplicate> potentialDuplicates) {
        this.message = message;
        this.potentialDuplicates = potentialDuplicates;
    }

    // Nested class to hold essential information for each potential duplicate
    public static class PotentialDuplicate {
        private Long id;
        private String opportunityName;
        private LocalDate submissionDate;
        private String businessSegment;
        private String fiscalYear;
        private String quarter;

        public PotentialDuplicate(Long id, String opportunityName, LocalDate submissionDate,
                                  String businessSegment, String fiscalYear, String quarter) {
            this.id = id;
            this.opportunityName = opportunityName;
            this.submissionDate = submissionDate;
            this.businessSegment = businessSegment;
            this.fiscalYear = fiscalYear;
            this.quarter = quarter;
        }

        // Getters
        public Long getId() {
            return id;
        }

        public String getOpportunityName() {
            return opportunityName;
        }

        public LocalDate getSubmissionDate() {
            return submissionDate;
        }

        public String getBusinessSegment() {
            return businessSegment;
        }

        public String getFiscalYear() {
            return fiscalYear;
        }

        public String getQuarter() {
            return quarter;
        }

        // Setters
        public void setId(Long id) {
            this.id = id;
        }

        public void setOpportunityName(String opportunityName) {
            this.opportunityName = opportunityName;
        }

        public void setSubmissionDate(LocalDate submissionDate) {
            this.submissionDate = submissionDate;
        }

        public void setBusinessSegment(String businessSegment) {
            this.businessSegment = businessSegment;
        }

        public void setFiscalYear(String fiscalYear) {
            this.fiscalYear = fiscalYear;
        }

        public void setQuarter(String quarter) {
            this.quarter = quarter;
        }
    }

    // Getters and setters for DuplicateOpportunityResponse
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PotentialDuplicate> getPotentialDuplicates() {
        return potentialDuplicates;
    }

    public void setPotentialDuplicates(List<PotentialDuplicate> potentialDuplicates) {
        this.potentialDuplicates = potentialDuplicates;
    }
}