package com.portal.bid.service.implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.bid.dto.BusinessSegmentDTO;
import com.portal.bid.dto.DealDTO;
import com.portal.bid.dto.FyDTO;
import com.portal.bid.dto.GoNoGoMasterDTO;
import com.portal.bid.dto.LeadDTO;
import com.portal.bid.dto.LeadFilterDTO;
import com.portal.bid.dto.LeadMultiFilterDTO;
import com.portal.bid.dto.LeadResponseDTO;
import com.portal.bid.dto.PriorityDTO;
import com.portal.bid.dto.UserDTO;
import com.portal.bid.entity.BidTracker;
import com.portal.bid.entity.Lead;
import com.portal.bid.entity.User;
import com.portal.bid.repository.BidTrackerRepository;
import com.portal.bid.repository.BusinessSegmentRepository;
import com.portal.bid.repository.DealRepository;
import com.portal.bid.repository.FyRepository;
import com.portal.bid.repository.GoNoGoMasterRepository;
import com.portal.bid.repository.LeadRepository;
import com.portal.bid.repository.PriorityRepo;
import com.portal.bid.repository.UserRepository;
import com.portal.bid.service.LeadService;
import com.portal.bid.specification.LeadSpecification;
import com.portal.bid.util.BidTrackerMapper;

// Service implementation
@Service
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final FyRepository fyRepository;
    private final BusinessSegmentRepository businessSegmentRepository;
    private final DealRepository dealRepository;
    private final PriorityRepo priorityRepository;
    private final GoNoGoMasterRepository goNoGoMasterRepository;
    private final UserRepository userRepository;
    @Autowired
    private BidTrackerRepository bidTrackerRepository;
    
    @Autowired
    public LeadServiceImpl(LeadRepository leadRepository, 
                          FyRepository fyRepository,
                          BusinessSegmentRepository businessSegmentRepository,
                          DealRepository dealRepository,
                          PriorityRepo priorityRepository,
                          GoNoGoMasterRepository goNoGoMasterRepository,
                          UserRepository userRepository) {
        this.leadRepository = leadRepository;
        this.fyRepository = fyRepository;
        this.businessSegmentRepository = businessSegmentRepository;
        this.dealRepository = dealRepository;
        this.priorityRepository = priorityRepository;
        this.goNoGoMasterRepository = goNoGoMasterRepository;
        this.userRepository = userRepository;
    }

    
    @Override
    @Transactional
    public LeadResponseDTO createLead(LeadDTO leadDTO, Long currentUserId) {
        Lead lead = convertToEntity(leadDTO);
        
        // Set the created by user
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUserId));
        lead.setCreatedBy(currentUser);
        lead.setLastModifiedBy(currentUser);
        
        Lead savedLead = leadRepository.save(lead);
        // Create and save the BidTracker for this lead
        BidTracker bidTracker = BidTrackerMapper.fromLead(savedLead, currentUser);
        bidTrackerRepository.save(bidTracker);

        return convertToResponseDTO(savedLead);
    }

    @Override
    @Transactional
    public LeadResponseDTO updateLead(Long id, LeadDTO leadDTO, Long currentUserId) {
        Lead existingLead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        
        // Update fields
        updateLeadFields(existingLead, leadDTO);
        
        // Set the last modified by user
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUserId));
        existingLead.setLastModifiedBy(currentUser);
        
        Lead updatedLead = leadRepository.save(existingLead);
        // Find and update the associated BidTracker
        BidTracker bidTracker = bidTrackerRepository.findByLead(updatedLead);
        if (bidTracker != null) {
            // Update relevant fields from the lead
            bidTracker.setRfpReleaseDate(updatedLead.getRfpReleaseDate());
            bidTracker.setGoNoGo(updatedLead.getGoNoGoMaster());
            bidTracker.setTenderSubmissionDate(updatedLead.getBidSubmissionDate());
            bidTracker.setDealStatus(updatedLead.getDealStatus());
            bidTracker.setLastModifiedBy(currentUser);
            
            // Save the updated bid tracker
            bidTrackerRepository.save(bidTracker);
        } else {
            // If no BidTracker exists for this Lead, create one
            BidTracker newBidTracker = BidTrackerMapper.fromLead(updatedLead, currentUser);
            bidTrackerRepository.save(newBidTracker);
        }
        return convertToResponseDTO(updatedLead);
    }

    @Override
    public LeadResponseDTO getLeadById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        return convertToResponseDTO(lead);
    }

    @Override
    @Transactional
    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
    }

    @Override
    public Page<LeadResponseDTO> getAllLeads(Pageable pageable) {
        Page<Lead> leadsPage = leadRepository.findAll(pageable);
        return leadsPage.map(this::convertToResponseDTO);
    }

    @Override
    public Page<LeadResponseDTO> getFilteredLeads(LeadFilterDTO filterDTO, Pageable pageable) {
        Specification<Lead> spec = LeadSpecification.buildSpecification(
                filterDTO.getFyId(),
                filterDTO.getObFyId(),
                filterDTO.getIndustrySegmentId(),
                filterDTO.getPublicPrivate(),
                filterDTO.getDealStatusId(),
                filterDTO.getPriorityId(),
                filterDTO.getPrimaryOfferingSegment(),
                filterDTO.getMinAmount(),
                filterDTO.getMaxAmount(),
                filterDTO.getStartDate(),
                filterDTO.getEndDate(),
                filterDTO.getMinProbability()
        );
        
        Page<Lead> leadsPage = leadRepository.findAll(spec, pageable);
        return leadsPage.map(this::convertToResponseDTO);
    }
    private Lead convertToEntity(LeadDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("LeadDTO cannot be null");
        }
    
        Lead lead = new Lead();
        lead.setId(dto.getId());
    
        // Set fields from DTO
        lead.setOpportunityName(dto.getOpportunityName());
        lead.setPartQuarter(dto.getPartQuarter());
        lead.setPartMonth(dto.getPartMonth());
        lead.setObQtr(dto.getObQtr());
        lead.setObMmm(dto.getObMmm());
        lead.setPublicPrivate(dto.getPublicPrivate());
        lead.setPrimaryOfferingSegment(dto.getPrimaryOfferingSegment());
        lead.setSecondaryOfferingSegment(dto.getSecondaryOfferingSegment());
        lead.setProjectTenureMonths(dto.getProjectTenureMonths());
        lead.setEstCapexInrCr(dto.getEstCapexInrCr());
        lead.setEstOpexInrCr(dto.getEstOpexInrCr());
        lead.setOpexTenureMonths(dto.getOpexTenureMonths());
        lead.setGoNoGoDate(dto.getGoNoGoDate());
        lead.setGmPercentage(dto.getGmPercentage());
        lead.setProbability(dto.getProbability());
        lead.setPrimaryOwner(dto.getPrimaryOwner());
        lead.setSolutionSpoc(dto.getSolutionSpoc());
        lead.setScmSpoc(dto.getScmSpoc());
        lead.setRemarks(dto.getRemarks());
        lead.setPqTq_remarks(dto.getPqTq_remarks());
        lead.setRfpReleaseDate(dto.getRfpReleaseDate());
        lead.setBidSubmissionDate(dto.getBidSubmissionDate());
    
        // Set numerical fields with validation
        if (dto.getAmount() != null && dto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        lead.setAmount(dto.getAmount());
    
        if (dto.getProbability() != null && (dto.getProbability() < 0 || dto.getProbability() > 100)) {
            throw new IllegalArgumentException("Probability must be between 0 and 100");
        }
        lead.setProbability(dto.getProbability());
    
        lead.setActualBookedOb(dto.getActualBookedOb());
        lead.setActualBookedCapex(dto.getActualBookedCapex());
        lead.setActualBookedOpex(dto.getActualBookedOpex());
        lead.setRevInObQtr(dto.getRevInObQtr());
        lead.setRevInObQtrPlus1(dto.getRevInObQtrPlus1());
    
        // Set references to other entities using repositories
        try {
            if (dto.getPartFyId() != null) {
                lead.setPartFy(fyRepository.findById(dto.getPartFyId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Part FY not found with id: " + dto.getPartFyId())));
            }
    
            if (dto.getObFyId() != null) {
                lead.setObFy(fyRepository.findById(dto.getObFyId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("OB FY not found with id: " + dto.getObFyId())));
            }
    
            if (dto.getIndustrySegmentId() != null) {
                lead.setIndustrySegment(businessSegmentRepository.findById(dto.getIndustrySegmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Industry segment not found with id: " + dto.getIndustrySegmentId())));
            }
    
            if (dto.getDealStatusId() != null) {
                lead.setDealStatus(dealRepository.findById(dto.getDealStatusId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Deal status not found with id: " + dto.getDealStatusId())));
            }
    
            if (dto.getPriorityId() != null) {
                lead.setPriority(priorityRepository.findById(dto.getPriorityId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + dto.getPriorityId())));
            }
    
            if (dto.getGoNoGoStatusId() != null) {
                lead.setGoNoGoMaster(goNoGoMasterRepository.findById(dto.getGoNoGoStatusId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Go/No-Go Master not found with id: " + dto.getGoNoGoStatusId())));
            }
        } catch (ResourceNotFoundException ex) {
            throw new IllegalArgumentException("Invalid reference: " + ex.getMessage(), ex);
        }
    
        return lead;
    }
    private void updateLeadFields(Lead existingLead, LeadDTO dto) {
        // Only update fields that are not null in the DTO
        if (dto.getOpportunityName() != null) {
            existingLead.setOpportunityName(dto.getOpportunityName());
        }
    
        if (dto.getPartFyId() != null) {
            existingLead.setPartFy(fyRepository.findById(dto.getPartFyId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Part FY not found with id: " + dto.getPartFyId())));
        }
    
        if (dto.getPartQuarter() != null) {
            existingLead.setPartQuarter(dto.getPartQuarter());
        }
    
        if (dto.getPartMonth() != null) {
            existingLead.setPartMonth(dto.getPartMonth());
        }
    
        if (dto.getObFyId() != null) {
            existingLead.setObFy(fyRepository.findById(dto.getObFyId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("OB FY not found with id: " + dto.getObFyId())));
        }
    
        if (dto.getObQtr() != null) {
            existingLead.setObQtr(dto.getObQtr());
        }
    
        if (dto.getObMmm() != null) {
            existingLead.setObMmm(dto.getObMmm());
        }
    
        if (dto.getIndustrySegmentId() != null) {
            existingLead.setIndustrySegment(businessSegmentRepository.findById(dto.getIndustrySegmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Industry segment not found with id: " + dto.getIndustrySegmentId())));
        }
    
        if (dto.getPublicPrivate() != null) {
            existingLead.setPublicPrivate(dto.getPublicPrivate());
        }
    
        if (dto.getDealStatusId() != null) {
            existingLead.setDealStatus(dealRepository.findById(dto.getDealStatusId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Deal status not found with id: " + dto.getDealStatusId())));
        }
    
        if (dto.getPriorityId() != null) {
            existingLead.setPriority(priorityRepository.findById(dto.getPriorityId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + dto.getPriorityId())));
        }
    
        if (dto.getAmount() != null) {
            existingLead.setAmount(dto.getAmount());
        }
    
        if (dto.getProbability() != null) {
            if (dto.getProbability() < 0 || dto.getProbability() > 100) {
                throw new IllegalArgumentException("Probability must be between 0 and 100");
            }
            existingLead.setProbability(dto.getProbability());
        }
    
        if (dto.getPrimaryOfferingSegment() != null) {
            existingLead.setPrimaryOfferingSegment(dto.getPrimaryOfferingSegment());
        }
    
        if (dto.getSecondaryOfferingSegment() != null) {
            existingLead.setSecondaryOfferingSegment(dto.getSecondaryOfferingSegment());
        }
    
        if (dto.getProjectTenureMonths() != null) {
            existingLead.setProjectTenureMonths(dto.getProjectTenureMonths());
        }
    
        if (dto.getEstCapexInrCr() != null) {
            existingLead.setEstCapexInrCr(dto.getEstCapexInrCr());
        }
    
        if (dto.getEstOpexInrCr() != null) {
            existingLead.setEstOpexInrCr(dto.getEstOpexInrCr());
        }
    
        if (dto.getOpexTenureMonths() != null) {
            existingLead.setOpexTenureMonths(dto.getOpexTenureMonths());
        }
    
        if (dto.getGoNoGoStatusId() != null) {
            existingLead.setGoNoGoMaster(goNoGoMasterRepository.findById(dto.getGoNoGoStatusId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Go/No-Go Master not found with id: " + dto.getGoNoGoStatusId())));
        }
        
        if (dto.getGoNoGoDate() != null) {
            existingLead.setGoNoGoDate(dto.getGoNoGoDate());
        }
    
        if (dto.getGmPercentage() != null) {
            existingLead.setGmPercentage(dto.getGmPercentage());
        }
    
        if (dto.getPrimaryOwner() != null) {
            existingLead.setPrimaryOwner(dto.getPrimaryOwner());
        }
    
        if (dto.getSolutionSpoc() != null) {
            existingLead.setSolutionSpoc(dto.getSolutionSpoc());
        }

        if (dto.getScmSpoc() != null) {
            existingLead.setScmSpoc(dto.getScmSpoc());
        }

        if (dto.getRemarks() != null) {
            existingLead.setRemarks(dto.getRemarks());
        }

         if (dto.getPqTq_remarks() != null) {
            existingLead.setPqTq_remarks(dto.getPqTq_remarks());
        }
    
        if (dto.getRfpReleaseDate() != null) {
            existingLead.setRfpReleaseDate(dto.getRfpReleaseDate());
        }
    
        if (dto.getBidSubmissionDate() != null) {
            existingLead.setBidSubmissionDate(dto.getBidSubmissionDate());
        }
    
        if (dto.getActualBookedOb() != null) {
            existingLead.setActualBookedOb(dto.getActualBookedOb());
        }
    
        if (dto.getActualBookedCapex() != null) {
            existingLead.setActualBookedCapex(dto.getActualBookedCapex());
        }
    
        if (dto.getActualBookedOpex() != null) {
            existingLead.setActualBookedOpex(dto.getActualBookedOpex());
        }
    
        if (dto.getRevInObQtr() != null) {
            existingLead.setRevInObQtr(dto.getRevInObQtr());
        }
    
        if (dto.getRevInObQtrPlus1() != null) {
            existingLead.setRevInObQtrPlus1(dto.getRevInObQtrPlus1());
        }
    }
    private LeadResponseDTO convertToResponseDTO(Lead lead) {
        return LeadResponseDTO.builder()
            .id(lead.getId())
            .opportunityName(lead.getOpportunityName())
            .partFy(lead.getPartFy() != null ? FyDTO.builder().id((long) lead.getPartFy().getId()).obFy(lead.getPartFy().getObFy()).build() : null)
            .partQuarter(lead.getPartQuarter())
            .partMonth(lead.getPartMonth())
            .obFy(lead.getObFy() != null ? FyDTO.builder().id((long) lead.getObFy().getId()).obFy(lead.getObFy().getObFy()).build() : null)
            .obQtr(lead.getObQtr())
            .obMmm(lead.getObMmm())
            .priority(lead.getPriority() != null ? new PriorityDTO(lead.getPriority().getId().longValue(), lead.getPriority().getPriority()) : null)
            .amount(lead.getAmount())
            .dealStatus(lead.getDealStatus() != null ? new DealDTO(lead.getDealStatus().getId(), lead.getDealStatus().getDealStatus()) : null)
            .actualBookedOb(lead.getActualBookedOb())
            .actualBookedCapex(lead.getActualBookedCapex())
            .actualBookedOpex(lead.getActualBookedOpex())
            .revInObQtr(lead.getRevInObQtr())
            .revInObQtrPlus1(lead.getRevInObQtrPlus1())
            .industrySegment(lead.getIndustrySegment() != null ? new BusinessSegmentDTO(lead.getIndustrySegment().getId(), lead.getIndustrySegment().getName()) : null)
            .publicPrivate(lead.getPublicPrivate())
            .primaryOfferingSegment(lead.getPrimaryOfferingSegment())
            .secondaryOfferingSegment(lead.getSecondaryOfferingSegment())
            .projectTenureMonths(lead.getProjectTenureMonths())
            .estCapexInrCr(lead.getEstCapexInrCr())
            .estOpexInrCr(lead.getEstOpexInrCr())
            .opexTenureMonths(lead.getOpexTenureMonths())
            .goNoGoMaster(lead.getGoNoGoMaster() != null ? new GoNoGoMasterDTO(lead.getGoNoGoMaster().getId(), lead.getGoNoGoMaster().getDealStatus()) : null)
            .goNoGoDate(lead.getGoNoGoDate())
            .gmPercentage(lead.getGmPercentage())
            .probability(lead.getProbability())
            .primaryOwner(lead.getPrimaryOwner())
            .solutionSpoc(lead.getSolutionSpoc())
            .scmSpoc(lead.getScmSpoc())
            .remarks(lead.getRemarks())
            .pqTq_remarks(lead.getPqTq_remarks())

            .rfpReleaseDate(lead.getRfpReleaseDate())
            .bidSubmissionDate(lead.getBidSubmissionDate())
            .createdBy(lead.getCreatedBy() != null ? new UserDTO(lead.getCreatedBy().getId(), lead.getCreatedBy().getFirstName(), lead.getCreatedBy().getLastName()) : null)
            .createdDate(lead.getCreatedDate())
            .lastModifiedDate(lead.getLastModifiedDate())
            .lastModifiedBy(lead.getLastModifiedBy() != null ? new UserDTO(lead.getLastModifiedBy().getId(), lead.getLastModifiedBy().getFirstName(), lead.getLastModifiedBy().getLastName()) : null)
            .build();
    }


    @Override
    public Page<LeadResponseDTO> getLeadsBySpecification(Specification<Lead> spec, Pageable pageable) {
        Page<Lead> leads = leadRepository.findAll(spec, pageable);
        return leads.map(this::convertToResponseDTO); // Convert each Lead to DTO
    }

    
    @Override
    public Page<LeadResponseDTO> getLeadsByMultiFilters(LeadMultiFilterDTO filterDTO, Pageable pageable) {
        Specification<Lead> spec = buildMultiFilterSpecification(filterDTO);
        Page<Lead> leadsPage = leadRepository.findAll(spec, pageable);
        return leadsPage.map(this::convertToResponseDTO);
    }

    private Specification<Lead> buildMultiFilterSpecification(LeadMultiFilterDTO filterDTO) {
        return Specification.where(inFyIds(filterDTO.getFyIds()))
                .and(inDealStatusIds(filterDTO.getDealStatusIds()))
                .and(inIndustrySegmentIds(filterDTO.getIndustrySegmentIds()))
                .and(inPartFyIds(filterDTO.getPartFyids()));
    }

    private Specification<Lead> inFyIds(List<Long> fyIds) {
        return (root, query, cb) -> {
            if (fyIds == null || fyIds.isEmpty()) return cb.conjunction();
            return root.get("obFy").get("id").in(fyIds);
        };
    }

    private Specification<Lead> inPartFyIds(List<Long> fyIds) {
        return (root, query, cb) -> {
            if (fyIds == null || fyIds.isEmpty()) return cb.conjunction();
            return root.get("partFy").get("id").in(fyIds);
        };
    }

    private Specification<Lead> inDealStatusIds(List<Long> dealStatusIds) {
        return (root, query, cb) -> {
            if (dealStatusIds == null || dealStatusIds.isEmpty()) return cb.conjunction();
            return root.get("dealStatus").get("id").in(dealStatusIds);
        };
    }

    private Specification<Lead> inIndustrySegmentIds(List<Long> industrySegmentIds) {
        return (root, query, cb) -> {
            if (industrySegmentIds == null || industrySegmentIds.isEmpty()) return cb.conjunction();
            return root.get("industrySegment").get("id").in(industrySegmentIds);
        };
    }

    @Override
    public List<LeadResponseDTO> getAllLeadsByMultiFilters(LeadMultiFilterDTO filterDTO, Sort sort) {
    Specification<Lead> spec = buildMultiFilterSpecification(filterDTO);
    List<Lead> leads = leadRepository.findAll(spec, sort);
    return leads.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
}


    @Override
    public List<String> getAllSalesOwners() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSalesOwners'");
    }

}

