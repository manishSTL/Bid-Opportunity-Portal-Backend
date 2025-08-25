package com.portal.bid.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidTrackerDTO {
    private Long id;
    private Long leadId;
    private Boolean opportunityIdentification;
    private LocalDate rfpReleaseDate;
    private LocalDate preBidMeeting;
    private Integer goNoGoId;
    private String goNoGoName;
    private Boolean querySubmission;
    private Boolean solutionReady;
    private LocalDate solutionReadyDate;
    private Boolean mouReady;
    private Boolean pricingDone;
    private LocalDate pricingDoneDate;
    private LocalDate tenderSubmissionDate;
    private LocalDate bidOpening;
    private Integer dealStatusId;
    private String dealStatusName;
    private Boolean pdTq;
    private Boolean boqReadiness;
    private LocalDateTime createdDate;
    private Integer createdBy;
    private String createdByName;
    private LocalDateTime lastModifiedDate;
    private Integer lastModifiedBy;
    private String lastModifiedByName;
}
