package com.portal.bid.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for creating and updating leads
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadDTO {
    private Long id;
    private String opportunityName;
    private Long partFyId;
    private String partQuarter;
    private String partMonth;
    private Long obFyId;
    private String obQtr;
    private String obMmm;
    private Long priorityId;
    private BigDecimal amount;
    private Long dealStatusId;
    private BigDecimal actualBookedOb;
    private BigDecimal actualBookedCapex;
    private BigDecimal actualBookedOpex;
    private BigDecimal revInObQtr;
    private BigDecimal revInObQtrPlus1;
    private Long industrySegmentId;
    private String publicPrivate;
    private String primaryOfferingSegment;
    private String secondaryOfferingSegment;
    private Integer projectTenureMonths;
    private BigDecimal estCapexInrCr;
    private BigDecimal estOpexInrCr;
    private Integer opexTenureMonths;
    private Long goNoGoStatusId;
    private LocalDate goNoGoDate;
    private BigDecimal gmPercentage;
    private Integer probability;
    private String primaryOwner;
    private String solutionSpoc;
    private LocalDate rfpReleaseDate;
    private LocalDate bidSubmissionDate;
}

