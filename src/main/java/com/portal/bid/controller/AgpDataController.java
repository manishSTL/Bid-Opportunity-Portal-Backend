package com.portal.bid.controller;

import com.portal.bid.entity.AgpData;
import com.portal.bid.service.AgpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agp")
public class AgpDataController {

    @Autowired
    private AgpDataService agpDataService;

    @PostMapping
    public ResponseEntity<AgpData> addAgpData(@RequestBody AgpData agpData) {
        AgpData savedAgpData = agpDataService.addAgpData(agpData);
        return new ResponseEntity<>(savedAgpData, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgpData> editAgpData(@PathVariable Long id, @RequestBody AgpData agpData) {
        return agpDataService.editAgpData(id, agpData)
                .map(ResponseEntity::ok)
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
}