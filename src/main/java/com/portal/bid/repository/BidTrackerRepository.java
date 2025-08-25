package com.portal.bid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.bid.entity.BidTracker;
import com.portal.bid.entity.Lead;

@Repository
public interface BidTrackerRepository extends JpaRepository<BidTracker, Long> {
    
    // Find BidTracker by associated Lead
    BidTracker findByLead(Lead lead);
    
    // Optional: Find BidTracker by Lead ID
    BidTracker findByLeadId(Long leadId);
}