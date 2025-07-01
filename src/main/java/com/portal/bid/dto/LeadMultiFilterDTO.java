package com.portal.bid.dto;

import java.util.List;

import lombok.Data;

@Data
public class LeadMultiFilterDTO {
    private List<Long> fyIds;
    private List<Long> dealStatusIds;
    private List<Long> industrySegmentIds;
    private List<Long> partFyids;
}