package com.portal.bid.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bid.dto.ApiResponse;
import com.portal.bid.dto.LeadAmountSummaryDTO;
import com.portal.bid.dto.LeadDTO;
import com.portal.bid.dto.LeadFilterDTO;
import com.portal.bid.dto.LeadMultiFilterDTO;
import com.portal.bid.dto.LeadResponseDTO;
import com.portal.bid.dto.PaginatedResponse;
import com.portal.bid.entity.Fy;
import com.portal.bid.entity.Lead;
import com.portal.bid.entity.User;
import com.portal.bid.service.FyService;
import com.portal.bid.service.LeadService;
import com.portal.bid.service.UserService;
import com.portal.bid.specification.LeadSpecification;
import com.portal.bid.util.LeadRequestMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leads")
@Validated
@Tag(name = "Lead Management", description = "APIs for managing sales leads and opportunities")
public class LeadController {

    @Autowired
    private LeadService leadService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private FyService fyService;
    
    @Operation(summary = "Create a new lead", description = "Creates a new sales opportunity lead in the system")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Lead created successfully",
            content = @Content(schema = @Schema(implementation = LeadResponseDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    
    @PostMapping
    public ResponseEntity<LeadResponseDTO> createLead(
            @Valid @RequestBody LeadDTO leadDTO) {
        
        // Get authenticated user's email from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Fetch user using email and get the ID
        User user = userService.findUserByEmail(currentUserEmail);
        Long userId = (long) user.getId();  // Extract ID

        // Create lead
        LeadResponseDTO createdLead = leadService.createLead(leadDTO, userId);

        return new ResponseEntity<>(createdLead, HttpStatus.CREATED);
    }


    
    @Operation(summary = "Update an existing lead", description = "Updates an existing sales opportunity lead by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Lead updated successfully",
            content = @Content(schema = @Schema(implementation = LeadResponseDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Lead not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })

    // @PutMapping("/{id}")
    // public ResponseEntity<LeadResponseDTO> updateLead(
    //         @PathVariable Long id,
    //         @Valid @RequestBody LeadDTO leadDTO) {
    //     // Get authenticated user's email from SecurityContext
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     String currentUserEmail = authentication.getName();

    //     // Fetch user using email and get the ID
    //     User user = userService.findUserByEmail(currentUserEmail);
    //     Long userId = (long) user.getId();
    //     LeadResponseDTO updatedLead = leadService.updateLead(id, leadDTO, userId);
    //     return ResponseEntity.ok(updatedLead);
    // }
    @PutMapping("/{id}")
    public ResponseEntity<LeadResponseDTO> updateLead(
            @PathVariable Long id,
            @RequestBody String leadRequestBody) {
        // Map request JSON to LeadDTO
        LeadDTO leadDTO = LeadRequestMapper.mapJsonToLeadDTO(leadRequestBody);
        
        // Get authenticated user's email from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Fetch user using email and get the ID
        User user = userService.findUserByEmail(currentUserEmail);
        Long userId = (long) user.getId();
        LeadResponseDTO updatedLead = leadService.updateLead(id, leadDTO, userId);
        return ResponseEntity.ok(updatedLead);
    }
    
    @Operation(summary = "Get lead by ID", description = "Retrieves a specific lead by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Lead found",
            content = @Content(schema = @Schema(implementation = LeadResponseDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Lead not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<LeadResponseDTO> getLeadById(@PathVariable Long id) {
        LeadResponseDTO lead = leadService.getLeadById(id);
        return ResponseEntity.ok(lead);
    }
    
    @Operation(summary = "Delete lead", description = "Deletes a lead by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204", 
            description = "Lead deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Lead not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Get all leads with pagination", description = "Retrieves all leads with pagination support")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Leads retrieved successfully",
            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<PaginatedResponse<LeadResponseDTO>> getAllLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<LeadResponseDTO> leadsPage = leadService.getAllLeads(pageable);
        
        PaginatedResponse<LeadResponseDTO> response = PaginatedResponse.<LeadResponseDTO>builder()
                .content(leadsPage.getContent())
                .page(leadsPage.getNumber())
                .size(leadsPage.getSize())
                .totalElements(leadsPage.getTotalElements())
                .totalPages(leadsPage.getTotalPages())
                .last(leadsPage.isLast())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Search leads with filters", description = "Retrieves leads based on various filter criteria")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Filtered leads retrieved successfully",
            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
        )
    })

    
    @PostMapping("/search")
    public ResponseEntity<PaginatedResponse<LeadResponseDTO>> searchLeads(
            @RequestBody LeadFilterDTO filterDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<LeadResponseDTO> leadsPage = leadService.getFilteredLeads(filterDTO, pageable);
        
        PaginatedResponse<LeadResponseDTO> response = PaginatedResponse.<LeadResponseDTO>builder()
                .content(leadsPage.getContent())
                .page(leadsPage.getNumber())
                .size(leadsPage.getSize())
                .totalElements(leadsPage.getTotalElements())
                .totalPages(leadsPage.getTotalPages())
                .last(leadsPage.isLast())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Advanced search leads with multiple filters", description = "Retrieves leads based on multiple filter criteria with pagination")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Filtered leads retrieved successfully",
            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
        )
    })
    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse<LeadResponseDTO>> filterLeads(
            @RequestParam(required = false) Long fyId,
            @RequestParam(required = false) Long obFyId,
            @RequestParam(required = false) Long industrySegmentId,
            @RequestParam(required = false) String publicPrivate,
            @RequestParam(required = false) Long dealStatusId,
            @RequestParam(required = false) Long priorityId,
            @RequestParam(required = false) String primaryOfferingSegment,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer minProbability,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Specification<Lead> spec = LeadSpecification.buildSpecification(
                fyId, obFyId, industrySegmentId, publicPrivate, dealStatusId, 
                priorityId, primaryOfferingSegment, minAmount, maxAmount, 
                startDate, endDate, minProbability);
        
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // This requires additional method in LeadService that accepts Specification
        Page<LeadResponseDTO> leadsPage = leadService.getLeadsBySpecification(spec, pageable);
        
        PaginatedResponse<LeadResponseDTO> response = PaginatedResponse.<LeadResponseDTO>builder()
                .content(leadsPage.getContent())
                .page(leadsPage.getNumber())
                .size(leadsPage.getSize())
                .totalElements(leadsPage.getTotalElements())
                .totalPages(leadsPage.getTotalPages())
                .last(leadsPage.isLast())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter leads by multiple IDs", description = "Retrieves leads based on lists of FY IDs, Deal Status IDs, Industry Segment IDs")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Filtered leads retrieved successfully",
            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
        )
    })
    @PostMapping("/filter-by-ids")
    public ResponseEntity<PaginatedResponse<LeadResponseDTO>> filterLeadsByIds(
            @RequestBody LeadMultiFilterDTO filterDTO,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // Fetch all data if page & size are null
        if (page == null || size == null) {
            List<LeadResponseDTO> allLeads = leadService.getAllLeadsByMultiFilters(filterDTO, sort);
            return ResponseEntity.ok(new PaginatedResponse<>(allLeads, 0, allLeads.size(), allLeads.size(), 1, true));
        }

        // Otherwise, apply pagination
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LeadResponseDTO> leadsPage = leadService.getLeadsByMultiFilters(filterDTO, pageable);

        PaginatedResponse<LeadResponseDTO> response = PaginatedResponse.<LeadResponseDTO>builder()
                .content(leadsPage.getContent())
                .page(leadsPage.getNumber())
                .size(leadsPage.getSize())
                .totalElements(leadsPage.getTotalElements())
                .totalPages(leadsPage.getTotalPages())
                .last(leadsPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Month-wise and Quarter-wise Amount Summation",
           description = "Retrieves aggregated sums of amounts based on months and quarters where dealStatus is 'Won'.")
        @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Aggregated amounts retrieved successfully",
                content = @Content(schema = @Schema(implementation = LeadAmountSummaryDTO.class))
            )
    })
    @PostMapping("/amount-summary")
    public ResponseEntity<LeadAmountSummaryDTO> getLeadAmountSummary(@RequestBody LeadMultiFilterDTO filterDTO) {
        
        // Fetch all leads based on FY IDs
        List<LeadResponseDTO> leads = leadService.getAllLeadsByMultiFilters(filterDTO, Sort.by(Sort.Direction.ASC, "obMmm"));
        
        // Filter only those leads where dealStatus is "Won"
        List<LeadResponseDTO> wonLeads = leads.stream()
            .filter(lead -> lead.getDealStatus() != null && "Won".equalsIgnoreCase(lead.getDealStatus().getDealStatus()))
            .collect(Collectors.toList());

        // Aggregate sum by month (obMmm)
        Map<String, BigDecimal> monthWiseSum = wonLeads.stream()
            .filter(lead -> lead.getObMmm() != null)
            .collect(Collectors.groupingBy(
                LeadResponseDTO::getObMmm, 
                Collectors.reducing(BigDecimal.ZERO, LeadResponseDTO::getAmount, BigDecimal::add)
            ));

        // Aggregate sum by quarter (obQtr)
        Map<String, BigDecimal> quarterWiseSum = wonLeads.stream()
            .filter(lead -> lead.getObQtr() != null)
            .collect(Collectors.groupingBy(
                LeadResponseDTO::getObQtr, 
                Collectors.reducing(BigDecimal.ZERO, LeadResponseDTO::getAmount, BigDecimal::add)
            ));

        // Construct response DTO
        LeadAmountSummaryDTO response = new LeadAmountSummaryDTO(monthWiseSum, quarterWiseSum);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Deal Status-wise Cumulative Amount Summation",
           description = "Retrieves aggregated sums of amounts based on dealStatus.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Aggregated amounts retrieved successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })

    @PostMapping("/participation-summary")
public ResponseEntity<Map<String, Object>> getParticipationSummary(@RequestBody LeadMultiFilterDTO filterDTO) {
    List<LeadResponseDTO> leads = leadService.getAllLeadsByMultiFilters(filterDTO, Sort.by(Sort.Direction.ASC, "obMmm"));
    
    // Count participation by month (total count of leads regardless of status)
    Map<String, Long> monthWiseCount = leads.stream()
        .filter(lead -> lead.getObMmm() != null)
        .collect(Collectors.groupingBy(
            LeadResponseDTO::getObMmm, 
            Collectors.counting()
        ));
    
    Map<String, Object> response = new HashMap<>();
    response.put("monthWiseCount", monthWiseCount);
    
    return ResponseEntity.ok(response);
}
    @PostMapping("/deal-status-summary")
    public ResponseEntity<Map<String, BigDecimal>> getDealStatusSummary(@RequestBody LeadMultiFilterDTO filterDTO) {
        
        // Fetch all leads based on filters
        List<LeadResponseDTO> leads = leadService.getAllLeadsByMultiFilters(filterDTO, Sort.by(Sort.Direction.ASC, "dealStatus"));

        // Group and sum by dealStatus while handling null values
        Map<String, BigDecimal> dealStatusWiseSum = leads.stream()
            .filter(lead -> lead.getDealStatus() != null && lead.getDealStatus().getDealStatus() != null)
            .collect(Collectors.groupingBy(
                lead -> lead.getDealStatus().getDealStatus(),
                Collectors.reducing(BigDecimal.ZERO, 
                    lead -> lead.getAmount() != null ? lead.getAmount() : BigDecimal.ZERO, 
                    BigDecimal::add
                )
            ));

        return ResponseEntity.ok(dealStatusWiseSum);
    }

    @Operation(summary = "Get Segment-wise Total Amount Summary", 
           description = "Retrieves aggregated sums of amounts by industry segment")
@ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", 
        description = "Segment summary retrieved successfully",
        content = @Content(schema = @Schema(implementation = Map.class))
    )
})
@PostMapping("/segment-summary")
public ResponseEntity<Map<String, BigDecimal>> getSegmentSummary(@RequestBody LeadMultiFilterDTO filterDTO) {
    
    // Fetch all leads based on filters
    List<LeadResponseDTO> leads = leadService.getAllLeadsByMultiFilters(filterDTO, Sort.by(Sort.Direction.ASC, "industrySegment"));

    // Group and sum by industry segment
    Map<String, BigDecimal> segmentWiseSum = leads.stream()
        .filter(lead -> lead.getIndustrySegment() != null && lead.getIndustrySegment().getName() != null)
        .collect(Collectors.groupingBy(
            lead -> lead.getIndustrySegment().getName(),
            Collectors.reducing(BigDecimal.ZERO, 
                lead -> lead.getAmount() != null ? lead.getAmount() : BigDecimal.ZERO, 
                BigDecimal::add
            )
        ));

    return ResponseEntity.ok(segmentWiseSum);
}

  @PostMapping("/sales-owner-deal-status")
  public ResponseEntity<Map<String, Long>> getSalesOwnerDealStatus(@RequestBody LeadMultiFilterDTO filterDTO) {
    List<LeadResponseDTO> leads = leadService.getAllLeadsByMultiFilters(filterDTO, Sort.by(Sort.Direction.ASC, "dealStatus"));
    
    // Filter by sales owner if specified and not "All" - FIXED VERSION
    String salesOwner = filterDTO.getSalesOwner() != null ? filterDTO.getSalesOwner().toString() : null;
    if (salesOwner != null && 
        !salesOwner.isEmpty() && 
        !"All".equalsIgnoreCase(salesOwner)) {
        leads = leads.stream()
            .filter(lead -> lead.getPrimaryOwner() != null && 
                           lead.getPrimaryOwner().equals(salesOwner))
            .collect(Collectors.toList());
    }
    
    Map<String, Long> dealStatusWiseCount = leads.stream()
        .filter(lead -> lead.getDealStatus() != null && lead.getDealStatus().getDealStatus() != null)
        .collect(Collectors.groupingBy(
            lead -> lead.getDealStatus().getDealStatus(),
            Collectors.counting()
        ));
    
    return ResponseEntity.ok(dealStatusWiseCount);
}

    @PostMapping("/sales-owner-summary")
     public ResponseEntity<Map<String, Long>> getSalesOwnerSummary(@RequestBody LeadMultiFilterDTO filterDTO) {
     // Fetch all leads based on filters
     List<LeadResponseDTO> leads = leadService.getAllLeadsByMultiFilters(filterDTO, Sort.by(Sort.Direction.ASC, "id"));
    
     // Group and count by primary owner
        Map<String, Long> salesOwnerWiseCount = leads.stream()
         .filter(lead -> lead.getPrimaryOwner() != null)
         .collect(Collectors.groupingBy(
            LeadResponseDTO::getPrimaryOwner,
            Collectors.counting()
         ));
    
      return ResponseEntity.ok(salesOwnerWiseCount);
    }

    @Operation(summary = "Get all unique sales owners", description = "Retrieves all unique sales owners from leads")
     @GetMapping("/sales-owners")
     public ResponseEntity<List<String>> getAllSalesOwners() {
    List<String> salesOwners = leadService.getAllUniqueSalesOwners();
    return ResponseEntity.ok(salesOwners);
   }
    
    @Operation(summary = "Get Year-to-Date Summary by Business Segment", 
       description = "Retrieves aggregated sums of won deal amounts by business segment for a specific fiscal year up to current date")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Summary retrieved successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    @PostMapping("/segment-ytd-summary")
    public ResponseEntity<Map<String, BigDecimal>> getSegmentYtdSummary(@RequestBody LeadMultiFilterDTO filterDTO) {
        // Fetch all leads based on filters (using obFyId from filterDTO)
        List<LeadResponseDTO> leads = leadService.getAllLeadsByMultiFilters(filterDTO, Sort.by(Sort.Direction.ASC, "industrySegment"));
        
        // Get current date information
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        

        // Determine quarters to include based on selected year vs current year
        List<String> quartersToInclude = new ArrayList<>();
        
        // Get the selected fiscal year from the filterDTO
        Long selectedFyId = null;
        if (filterDTO.getFyIds() != null && !filterDTO.getFyIds().isEmpty()) {
            selectedFyId = filterDTO.getFyIds().get(0);
        }
        
        // Find the fiscal year entity to get the actual year value
        Integer selectedYear = null;
        if (selectedFyId != null) {
            // This would require a service method to get the Fy entity by ID
            int id = Math.toIntExact(selectedFyId);
            Fy selectedFy = fyService.findById(id)
                                    .orElseThrow(() -> new RuntimeException("FY not found with ID: " + id));


            if (selectedFy != null) {
                // Assuming the Fy entity has a getYear() method or similar
                selectedYear = selectedFy.getObFy();
            }
        }
        
        // If selected year is earlier than current year, include all quarters
        if (selectedYear != null && selectedYear < currentYear) {
            quartersToInclude.add("Q1");
            quartersToInclude.add("Q2");
            quartersToInclude.add("Q3");
            quartersToInclude.add("Q4");
        } 
        // If selected year is current year, include quarters based on current month
        else if (selectedYear != null && selectedYear.equals(currentYear)) {
            
            if(currentMonth >=1 && currentMonth <=3) {
                quartersToInclude.add("Q1");
                quartersToInclude.add("Q2");
                quartersToInclude.add("Q3");
                quartersToInclude.add("Q4");
            }
            
            // Include Q4 (Jan-Mar) if current month is >= January
            if (currentMonth >= 4 && currentMonth <= 6) {
                quartersToInclude.add("Q1");
            }
            
            // Include Q1 (Apr-Jun) if current month is >= April
            if (currentMonth >= 7 && currentMonth <= 9) {
                quartersToInclude.add("Q1");
                quartersToInclude.add("Q2");
            }
            
            if (currentMonth >= 10 && currentMonth <= 12) {
                quartersToInclude.add("Q1");
                quartersToInclude.add("Q2");
                quartersToInclude.add("Q3");
            }
        }
        
        // Filter only "Won" leads in the specified quarters
        List<LeadResponseDTO> filteredLeads = leads.stream()
            .filter(lead -> 
                lead.getDealStatus() != null && 
                "Won".equalsIgnoreCase(lead.getDealStatus().getDealStatus()) &&
                lead.getObQtr() != null && 
                quartersToInclude.contains(lead.getObQtr()))
            .collect(Collectors.toList());
        
        // Aggregate sum by business segment
        Map<String, BigDecimal> segmentWiseSum = filteredLeads.stream()
            .filter(lead -> lead.getIndustrySegment() != null && lead.getIndustrySegment().getName() != null)
            .collect(Collectors.groupingBy(
                lead -> lead.getIndustrySegment().getName(),
                Collectors.reducing(BigDecimal.ZERO, 
                    lead -> lead.getAmount() != null ? lead.getAmount() : BigDecimal.ZERO, 
                    BigDecimal::add
                )
            ));
        
        return ResponseEntity.ok(segmentWiseSum);
    }

    

}