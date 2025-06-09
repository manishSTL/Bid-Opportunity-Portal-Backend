// DTO for lead responses
package com.portal.bid.dto;
import java.math.BigDecimal;
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
public class LeadResponseDTO {
    private Long id;
    private String opportunityName;
    private FyDTO partFy;
    private String partQuarter;
    private String partMonth;
    private FyDTO obFy;
    private String obQtr;
    private String obMmm;
    private PriorityDTO priority;
    private BigDecimal amount;
    private DealDTO dealStatus;
    private BigDecimal actualBookedOb;
    private BigDecimal actualBookedCapex;
    private BigDecimal actualBookedOpex;
    private BigDecimal revInObQtr;
    private BigDecimal revInObQtrPlus1;
    private BusinessSegmentDTO industrySegment;
    private String publicPrivate;
    private String primaryOfferingSegment;
    private String secondaryOfferingSegment;
    private Integer projectTenureMonths;
    private BigDecimal estCapexInrCr;
    private BigDecimal estOpexInrCr;
    private Integer opexTenureMonths;
    private GoNoGoMasterDTO goNoGoMaster;
    private LocalDate goNoGoDate;
    private BigDecimal gmPercentage;
    private Integer probability;
    private String primaryOwner;
    private String solutionSpoc;
    private LocalDate rfpReleaseDate;
    private LocalDate bidSubmissionDate;
    private UserDTO createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private UserDTO lastModifiedBy;
}