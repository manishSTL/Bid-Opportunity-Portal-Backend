package com.portal.bid.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.portal.bid.dto.LeadDTO;
import com.portal.bid.dto.LeadFilterDTO;
import com.portal.bid.dto.LeadMultiFilterDTO;
import com.portal.bid.dto.LeadResponseDTO;
import com.portal.bid.entity.Lead;

// Service interface
public interface LeadService {
    // CRUD operations
    LeadResponseDTO createLead(LeadDTO leadDTO, Long currentUserId);
    LeadResponseDTO updateLead(Long id, LeadDTO leadDTO, Long currentUserId);
    LeadResponseDTO getLeadById(Long id);
    void deleteLead(Long id);
    Page<LeadResponseDTO> getAllLeads(Pageable pageable);
    
    // Analytics operations
    // LeadAnalyticsDTO getAnalytics(LeadFilterDTO filterDTO);
    Page<LeadResponseDTO> getFilteredLeads(LeadFilterDTO filterDTO, Pageable pageable);
    // List<FyWiseAnalyticsDTO> getFyWiseAnalytics(LeadFilterDTO filterDTO);
    // List<IndustrySegmentAnalyticsDTO> getIndustrySegmentAnalytics(LeadFilterDTO filterDTO);
    // List<DealStatusAnalyticsDTO> getDealStatusAnalytics(LeadFilterDTO filterDTO);
    Page<LeadResponseDTO> getLeadsBySpecification(Specification<Lead> spec, Pageable pageable);
    Page<LeadResponseDTO> getLeadsByMultiFilters(LeadMultiFilterDTO filterDTO, Pageable pageable);
    public List<LeadResponseDTO> getAllLeadsByMultiFilters(LeadMultiFilterDTO filterDTO, Sort sort);
    List<String> getAllUniqueSalesOwners();
}

