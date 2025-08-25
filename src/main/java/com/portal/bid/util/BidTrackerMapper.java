package com.portal.bid.util;

import com.portal.bid.entity.BidTracker;
import com.portal.bid.entity.Lead;
import com.portal.bid.entity.User;

public class BidTrackerMapper {
    
    /**
     * Creates a new BidTracker instance from a Lead entity
     * 
     * @param lead The lead entity to create the bid tracker from
     * @param currentUser The user creating the bid tracker
     * @return A new BidTracker instance with values copied from the lead
     */
    public static BidTracker fromLead(Lead lead, User currentUser) {
        return BidTracker.builder()
                .lead(lead)
                .opportunityIdentification(true)
                .rfpReleaseDate(lead.getRfpReleaseDate())
                .goNoGo(lead.getGoNoGoMaster())
                .querySubmission(false)
                .solutionReady(false)
                .mouReady(false)
                .pricingDone(false)
                .tenderSubmissionDate(lead.getBidSubmissionDate())
                .dealStatus(lead.getDealStatus())
                .createdBy(currentUser)
                .lastModifiedBy(currentUser)  // Set the initial last modified by to the same user
                .build();
    }
}