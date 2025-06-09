package com.portal.bid.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bid_trackers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", nullable = false)
    private Lead lead;

    @Column(name = "opportunity_identification", nullable = false)
    @Builder.Default
    private Boolean opportunityIdentification = true;
    
    @Column(name = "rfp_release_date")
    private LocalDate rfpReleaseDate;
    
    @Column(name = "pre_bid_meeting")
    private LocalDate preBidMeeting;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "go_no_go")
    private GoNoGoMaster goNoGo;
    
    @Column(name = "query_submission", nullable = false)
    @Builder.Default
    private Boolean querySubmission = false;
    
    @Column(name = "solution_ready")
    @Builder.Default
    private Boolean solutionReady = false;
    
    @Column(name = "solution_ready_date")
    private LocalDate solutionReadyDate;
    
    @Column(name = "mou_ready", nullable = false)
    @Builder.Default
    private Boolean mouReady = false;
    
    @Column(name = "pricing_done")
    @Builder.Default
    private Boolean pricingDone = false;
    
    @Column(name = "pricing_done_date")
    private LocalDate pricingDoneDate;
    
    @Column(name = "tender_submission_date")
    private LocalDate tenderSubmissionDate;
    
    @Column(name = "bid_opening")
    private LocalDate bidOpening;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_status")
    private Deal dealStatus;
    
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;
}