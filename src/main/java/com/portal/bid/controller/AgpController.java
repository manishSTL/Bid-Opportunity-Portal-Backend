package com.portal.bid.controller;

import com.portal.bid.entity.Agp;
import com.portal.bid.service.AgpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

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