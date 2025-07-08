package com.portal.bid.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bid.entity.Agp;
import com.portal.bid.service.AgpService;

@RestController
@RequestMapping("/api/agp1")
public class AgpController {

    private final AgpService agpService;

    @Autowired
    public AgpController(AgpService agpService) {
        this.agpService = agpService;
    }

    @GetMapping
    public List<Agp> getAllAgps() {
        return agpService.findAll();
    }

    @GetMapping("/summary/{fy}")
    public Map<String, Integer> getAgpSummaryByFy(@PathVariable String fy) {
        return agpService.findAll().stream()
            .filter(agp -> fy.equals(agp.getObFY())) // Filter by FY
            .collect(Collectors.groupingBy(
                Agp::getObQT, // Group by Quarter (obQT)
                Collectors.summingInt(Agp::getAgpValue) // Sum agpValue for each quarter
            ));
    }

    @GetMapping("/ytd-summary/{fy}")
    public Map<String, Integer> getAgpYtdSummary(@PathVariable String fy) {
        int currentYear = LocalDate.now().getYear();
        String currentFy = "FY" + (LocalDate.now().getMonthValue() >= 4 ? currentYear + 1 : currentYear);
        boolean isCurrentFy = fy.equals(currentFy);

        List<String> validQuarters = isCurrentFy ? getQuartersUpToNow() : List.of("Q1", "Q2", "Q3", "Q4");

        return agpService.findAll().stream()
            .filter(agp -> fy.equals(agp.getObFY())) // Filter by FY
            .filter(agp -> validQuarters.contains(agp.getObQT())) // Filter by valid quarters
            .collect(Collectors.groupingBy(
                Agp::getAccountName, // Group by Account Name
                Collectors.summingInt(Agp::getAgpValue) // Sum agpValue
            ));
    }

    // Utility method to get valid quarters for YTD
    private List<String> getQuartersUpToNow() {
        int month = LocalDate.now().getMonthValue();
        if (month >= 4 && month <= 6) return List.of("Q1"); // Apr-Jun
        if (month >= 7 && month <= 9) return List.of("Q1", "Q2"); // Apr-Sep
        if (month >= 10 && month <= 12) return List.of("Q1", "Q2", "Q3"); // Apr-Dec
        return List.of("Q1", "Q2", "Q3", "Q4"); // Jan-Mar (Full year)
    }



    @GetMapping("/{id}")
    public ResponseEntity<Agp> getAgpById(@PathVariable Long id) {
        Optional<Agp> agp = agpService.findById(id);
        return agp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAgp(@RequestBody Agp agp) {
        if (!isWithinEditWindow()) {
            return createUnprocessableEntityResponse("AGP creation is only allowed in February and March.");
        }
        Agp createdAgp = agpService.save(agp);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAgp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAgp(@PathVariable Long id, @RequestBody Agp agpDetails) {
        if (!isWithinEditWindow()) {
            return createUnprocessableEntityResponse("AGP value updates are only allowed in February and March.");
        }

        Optional<Agp> agpOptional = agpService.findById(id);
        if (agpOptional.isPresent()) {
            Agp agp = agpOptional.get();
            updateAgpFields(agp, agpDetails);
            final Agp updatedAgp = agpService.save(agp);
            return ResponseEntity.ok(updatedAgp);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgp(@PathVariable Long id) {
        Optional<Agp> agpOptional = agpService.findById(id);
        if (agpOptional.isPresent()) {
            agpService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isWithinEditWindow() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        LocalDate windowStart = LocalDate.of(currentYear, Month.FEBRUARY, 1);
        LocalDate windowEnd = LocalDate.of(currentYear, Month.MARCH, 31);
        return !now.isBefore(windowStart) && !now.isAfter(windowEnd);
    }

    private ResponseEntity<?> createUnprocessableEntityResponse(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error-Message", message);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).headers(headers).build();
    }

    private void updateAgpFields(Agp agp, Agp agpDetails) {
        agp.setEmployeeID(agpDetails.getEmployeeID());
        agp.setAccountName(agpDetails.getAccountName());
        agp.setObFY(agpDetails.getObFY());
        agp.setObQT(agpDetails.getObQT());
        agp.setAgpValue(agpDetails.getAgpValue());
        agp.setCreatedBy(agpDetails.getCreatedBy());
        agp.setUpdatedBy(agpDetails.getUpdatedBy());
        agp.setStatus(agpDetails.getStatus());
        agp.setUpdatedAt(LocalDateTime.now());
    }
}