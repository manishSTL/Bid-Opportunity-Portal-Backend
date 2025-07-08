package com.portal.bid.dto;

import com.portal.bid.entity.Form;

import java.util.List;

public class OpportunitiesResponse {

    private int totalOpportunities;
    private List<Form> opportunities;

    public OpportunitiesResponse(int totalOpportunities, List<Form> opportunities) {
        this.totalOpportunities = totalOpportunities;
        this.opportunities = opportunities;
    }

    public int getTotalOpportunities() {
        return totalOpportunities;
    }

    public void setTotalOpportunities(int totalOpportunities) {
        this.totalOpportunities = totalOpportunities;
    }

    public List<Form> getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(List<Form> opportunities) {
        this.opportunities = opportunities;
    }
}
