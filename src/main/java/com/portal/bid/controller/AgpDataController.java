package com.portal.bid.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bid.entity.Agp;
import com.portal.bid.entity.Agp.Status;
import com.portal.bid.entity.AgpData;
import com.portal.bid.entity.BusinessSegment;
import com.portal.bid.entity.User;
import com.portal.bid.service.AgpDataService;
import com.portal.bid.service.AgpService;
import com.portal.bid.service.BusinessSegmentService;
import com.portal.bid.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agp")
public class AgpDataController {

    @Autowired
    private AgpService agpService;

    @Autowired
    private BusinessSegmentService businessSegmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private AgpDataService agpDataService;

    private Agp createAgpFromAgpData(AgpData agpData) {
        Agp agp = new Agp();
        
        // Map the fields according to the specified mapping
        agp.setAgpValue(agpData.getAgpValue().intValue());
        agp.setCreatedAt(agpData.getCreatedAt());
        agp.setUpdatedAt(agpData.getUpdatedAt());
        agp.setCreatedBy("Admin");
        agp.setUpdatedBy("Admin");
        User user = agpData.getUser();
        Optional<User> user1 = userService.getUserById((long) user.getId());

        user1.ifPresent(u -> agp.setEmployeeID(u.getEmployeeId()));
        
        agp.setObFY(agpData.getFinancialYear());
        agp.setObQT(agpData.getQuarter());
        agp.setStatus(Status.ACTIVE);
        BusinessSegment segment = agpData.getBusinessSegment();
        System.out.println("USer: =============================================================================       "+ user.getId());  // Check if segment is null
        String name = "NA";
        System.out.println("Business Segment: " + segment);  // Check if segment is null
        if (segment != null) {
            System.out.println("Business Segment Name: " + segment.getId());  // Check if name is null
            segment = businessSegmentService.getbyid(segment.getId());
            name = segment.getName();
        }

        agp.setAccountName(name);
        
        return agp;
    }

    @PostMapping
    public ResponseEntity<AgpData> addAgpData(@Valid @RequestBody AgpData agpData) {
        AgpData savedAgpData = agpDataService.addAgpData(agpData);
        Agp agp = createAgpFromAgpData(savedAgpData);
        agpService.save(agp);
        return new ResponseEntity<>(savedAgpData, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgpData> editAgpData(@PathVariable Long id, @Valid @RequestBody AgpData agpData) {
        return agpDataService.editAgpData(id, agpData)
                .map(updatedAgpData -> {
                    // Update corresponding Agp entry
                    Agp agp = createAgpFromAgpData(updatedAgpData);
                    agpService.updateAgp(agp);
                    return ResponseEntity.ok(updatedAgpData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/allAgp")
    public ResponseEntity<List<AgpData>> getAllAgpData() {
        List<AgpData> allAgpData = agpDataService.getAllAgpData();
        return ResponseEntity.ok(allAgpData);
    }

    @GetMapping
    public ResponseEntity<List<AgpData>> getAgpData(
            @RequestParam(required = false) String userId,
            @RequestParam Long departmentId,
            @RequestParam String financialYear,
            @RequestParam String quarter,
            @RequestParam Long businessSegmentId) {

        List<AgpData> agpDataList;

        if (userId != null && !userId.isEmpty()) {
            List<Long> userIds = Arrays.stream(userId.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            agpDataList = agpDataService.getAgpDataByUserIds(userIds, departmentId, financialYear, quarter, businessSegmentId);
        } else {
            agpDataList = agpDataService.getAgpDataByFilters(departmentId, financialYear, quarter, businessSegmentId);
        }

        return ResponseEntity.ok(agpDataList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AgpData> getAgpDataByUserId(
            @PathVariable Long userId,
            @RequestParam Long departmentId,
            @RequestParam String financialYear,
            @RequestParam String quarter,
            @RequestParam Long businessSegmentId) {
        return agpDataService.getAgpDataByUserIdAndFilters(userId, departmentId, financialYear, quarter, businessSegmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgpData(@PathVariable Long id) {
        boolean deleted = agpDataService.deleteAgpData(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}