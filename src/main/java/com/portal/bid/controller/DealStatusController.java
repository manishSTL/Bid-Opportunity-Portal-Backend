package com.portal.bid.controller;

import com.portal.bid.entity.BusinessUnit;
import com.portal.bid.entity.DealStatus;
import com.portal.bid.entity.GoNoGoStatus;
import com.portal.bid.service.BusinessUnitService;
import com.portal.bid.service.DealStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deal-status")
public class DealStatusController {

    @Autowired
    private DealStatusService dealStatusService;

//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<DealStatus> createDealStatus(@Valid @RequestBody DealStatus dealStatus) {
        System.out.println("herrrr1");
        DealStatus createdUnit = dealStatusService.createDealStatus(dealStatus);
        System.out.println("herrrr2");

        return new ResponseEntity<>(createdUnit, HttpStatus.CREATED);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<DealStatus> getDealStatusByIdById(@PathVariable Long id) {
        DealStatus unit = dealStatusService.getDealStatusById(id);
        return unit != null ? new ResponseEntity<>(unit, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
//    @GetMapping
//    public ResponseEntity<List<DealStatus>> getAllDealStatus() {
//        List<DealStatus> units = dealStatusService.getAllDealStatus();
//        return new ResponseEntity<>(units, HttpStatus.OK);
//    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<DealStatus> updateDealStatus(@PathVariable Long id,@Valid @RequestBody DealStatus dealStatus) {
        DealStatus updatedUnit = dealStatusService.updateDealStatus(id, dealStatus);
        return updatedUnit != null ? new ResponseEntity<>(updatedUnit, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealStatus(@PathVariable Long id) {
        boolean isDeleted = dealStatusService.deleteDealStatus(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    ResponseEntity<List<DealStatus>>  getAll(@RequestParam(required = false) Long form_id){
        List<DealStatus> entryFound;
        if (form_id != null) {
            entryFound = dealStatusService.getAllDealStatus1(form_id);
        } else {
            entryFound = dealStatusService.getAllDealStatus();
        }
        return ResponseEntity.ok(entryFound);
    }

    // Handle validation errors
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
