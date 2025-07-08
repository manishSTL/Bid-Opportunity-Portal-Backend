package com.portal.bid.controller;

import com.portal.bid.entity.BusinessUnit;
import com.portal.bid.service.BusinessUnitService;
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
@RequestMapping("/api/business-units")
public class BusinessUnitController {

    @Autowired
    private BusinessUnitService businessUnitService;

//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<BusinessUnit> createBusinessUnit(@Valid @RequestBody BusinessUnit businessUnit) {
        BusinessUnit createdUnit = businessUnitService.createBusinessUnit(businessUnit);
        return new ResponseEntity<>(createdUnit, HttpStatus.CREATED);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<BusinessUnit> getBusinessUnitById(@PathVariable Long id) {
        BusinessUnit unit = businessUnitService.getBusinessUnitById(id);
        return unit != null ? new ResponseEntity<>(unit, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<BusinessUnit>> getAllBusinessUnits() {
        List<BusinessUnit> units = businessUnitService.getAllBusinessUnits();
        return new ResponseEntity<>(units, HttpStatus.OK);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<BusinessUnit> updateBusinessUnit(@PathVariable Long id,@Valid @RequestBody BusinessUnit businessUnit) {
        BusinessUnit updatedUnit = businessUnitService.updateBusinessUnit(id, businessUnit);
        return updatedUnit != null ? new ResponseEntity<>(updatedUnit, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessUnit(@PathVariable Long id) {
        boolean isDeleted = businessUnitService.deleteBusinessUnit(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
