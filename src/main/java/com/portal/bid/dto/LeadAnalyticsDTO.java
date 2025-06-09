package com.portal.bid.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for analytics summary
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadAnalyticsDTO {
    private BigDecimal totalAmount;
    private BigDecimal totalBookedAmount;
    private Long totalLeadsCount;
    private List<FyWiseAnalyticsDTO> fyWiseAnalytics;
    private List<IndustrySegmentAnalyticsDTO> industrySegmentAnalytics;
    private List<DealStatusAnalyticsDTO> dealStatusAnalytics;
}