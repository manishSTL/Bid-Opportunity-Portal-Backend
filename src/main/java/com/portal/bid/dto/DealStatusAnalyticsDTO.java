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
public class DealStatusAnalyticsDTO {
    private String statusName;
    private BigDecimal totalAmount;
    private Long leadsCount;
    private BigDecimal avgProbability;
}
