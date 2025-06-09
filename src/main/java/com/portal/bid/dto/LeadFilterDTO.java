package com.portal.bid.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for analytics filtering
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadFilterDTO {
    private Long fyId;
    private Long obFyId;
    private Long industrySegmentId;
    private String publicPrivate;
    private Long dealStatusId;
    private Long priorityId;
    private String primaryOfferingSegment;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minProbability;
}