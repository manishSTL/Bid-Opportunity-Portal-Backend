package com.portal.bid.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadAmountSummaryDTO {
    private Map<String, BigDecimal> monthWiseAmount;  // Example: { Apr : 20, May: 30,....}
    private Map<String, BigDecimal> quarterWiseAmount; // Example: { Q1 : 100, Q2 : 200 ,,,}
}
