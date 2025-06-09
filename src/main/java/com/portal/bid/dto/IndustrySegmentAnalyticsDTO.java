package com.portal.bid.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndustrySegmentAnalyticsDTO {
    private String segmentName;
    private BigDecimal totalAmount;
    private Long leadsCount;
    private BigDecimal avgProbability;
}