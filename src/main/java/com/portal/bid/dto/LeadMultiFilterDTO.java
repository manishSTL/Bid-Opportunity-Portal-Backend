package com.portal.bid.dto;

import java.util.List;
import lombok.Data;

@Data
public class LeadMultiFilterDTO {
    private List<Long> fyIds;
    private List<Long> dealStatusIds;
    private List<Long> industrySegmentIds;
    private String salesOwner; // Add this field
    
    // Remove the broken getSalesOwner() method completely
    // Lombok @Data will auto-generate getter/setter
}