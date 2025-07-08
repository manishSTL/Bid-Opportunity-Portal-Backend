package com.portal.bid.service;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.bid.dto.BidTrackerDTO;
import com.portal.bid.entity.BidTracker;
import com.portal.bid.entity.User;
import com.portal.bid.repository.BidTrackerRepository;
import com.portal.bid.repository.DealRepository;
import com.portal.bid.repository.GoNoGoMasterRepository;

import jakarta.transaction.Transactional;

@Service
public class BidTrackerService {

    @Autowired
    private BidTrackerRepository bidTrackerRepository;
    
    @Autowired
    private GoNoGoMasterRepository goNoGoMasterRepository;
    
    @Autowired
    private DealRepository dealRepository;

    public List<BidTrackerDTO> getAllBidTrackers() {
        return bidTrackerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BidTrackerDTO getBidTrackerById(Long id) {
        BidTracker bidTracker = bidTrackerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BidTracker not found with id: " + id));
        return convertToDTO(bidTracker);
    }

    public BidTrackerDTO getBidTrackerByLeadId(Long leadId) {
        BidTracker bidTracker = bidTrackerRepository.findByLeadId(leadId);
        if (bidTracker == null) {
            throw new ResourceNotFoundException("BidTracker not found for lead id: " + leadId);
        }
        return convertToDTO(bidTracker);
    }
    

    @Transactional
    public BidTrackerDTO updateBidTracker(Long id, BidTrackerDTO dto, User currentUser) {
        BidTracker bidTracker = bidTrackerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BidTracker not found with id: " + id));
        
        // Update non-lead related fields only
        bidTracker.setOpportunityIdentification(dto.getOpportunityIdentification());
        bidTracker.setRfpReleaseDate(dto.getRfpReleaseDate());
        bidTracker.setPreBidMeeting(dto.getPreBidMeeting());
        
        if (dto.getGoNoGoId() != null) {
            bidTracker.setGoNoGo(goNoGoMasterRepository.findById(dto.getGoNoGoId())
                    .orElseThrow(() -> new ResourceNotFoundException("GoNoGo not found with id: " + dto.getGoNoGoId())));
        }
        
        bidTracker.setQuerySubmission(dto.getQuerySubmission());
        bidTracker.setSolutionReady(dto.getSolutionReady());
        bidTracker.setSolutionReadyDate(dto.getSolutionReadyDate());
        bidTracker.setMouReady(dto.getMouReady());
        bidTracker.setPricingDone(dto.getPricingDone());
        bidTracker.setPricingDoneDate(dto.getPricingDoneDate());
        bidTracker.setTenderSubmissionDate(dto.getTenderSubmissionDate());
        bidTracker.setBidOpening(dto.getBidOpening());
        bidTracker.setBoqReadiness(dto.getBoqReadiness());
        bidTracker.setPdTq(dto.getPdTq());
        if (dto.getDealStatusId() != null) {
            bidTracker.setDealStatus(dealRepository.findById(dto.getDealStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Deal status not found with id: " + dto.getDealStatusId())));
        }
        
        // Update last modified user
        bidTracker.setLastModifiedBy(currentUser);
        
        return convertToDTO(bidTrackerRepository.save(bidTracker));
    }

    private BidTrackerDTO convertToDTO(BidTracker entity) {
        return BidTrackerDTO.builder()
                .id(entity.getId())
                .leadId(entity.getLead() != null ? entity.getLead().getId() : null)
                .opportunityIdentification(entity.getOpportunityIdentification())
                .rfpReleaseDate(entity.getRfpReleaseDate())
                .preBidMeeting(entity.getPreBidMeeting())
                .goNoGoId(entity.getGoNoGo() != null ? entity.getGoNoGo().getId() : null)
                .goNoGoName(entity.getGoNoGo() != null ? entity.getGoNoGo().getDealStatus() : null)
                .querySubmission(entity.getQuerySubmission())
                .solutionReady(entity.getSolutionReady())
                .solutionReadyDate(entity.getSolutionReadyDate())
                .mouReady(entity.getMouReady())
                .pricingDone(entity.getPricingDone())
                .pricingDoneDate(entity.getPricingDoneDate())
                .tenderSubmissionDate(entity.getTenderSubmissionDate())
                .bidOpening(entity.getBidOpening())
                .dealStatusId(entity.getDealStatus() != null ? entity.getDealStatus().getId() : null)
                .dealStatusName(entity.getDealStatus() != null ? entity.getDealStatus().getDealStatus() : null)
                .pdTq(entity.getPdTq())
                .boqReadiness(entity.getBoqReadiness())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null)
                .createdByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getFirstName() : null)
                .lastModifiedDate(entity.getLastModifiedDate())
                .lastModifiedBy(entity.getLastModifiedBy() != null ? entity.getLastModifiedBy().getId() : null)
                .lastModifiedByName(entity.getLastModifiedBy() != null ? entity.getLastModifiedBy().getFirstName() : null)
                .build();
    }
}