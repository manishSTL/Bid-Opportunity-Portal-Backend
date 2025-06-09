package com.portal.bid.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.portal.bid.entity.GoNoGoMaster;
import com.portal.bid.service.GoNoGoMasterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/go-no-go")
public class GoNoGoMasterController {

    @Autowired
    private GoNoGoMasterService dealService;

//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<GoNoGoMaster> createDeal(@Valid @RequestBody GoNoGoMaster deal) {
        GoNoGoMaster createdDeal = dealService.createDeal(deal);
        return new ResponseEntity<>(createdDeal, HttpStatus.CREATED);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<GoNoGoMaster>> getAllDeals() {
        List<GoNoGoMaster> deals = dealService.getAllDeals();
        return new ResponseEntity<>(deals, HttpStatus.OK);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<GoNoGoMaster> getDealById(@PathVariable Integer id) {
        GoNoGoMaster deal = dealService.getDealById(id);
        return deal != null ? new ResponseEntity<>(deal, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<GoNoGoMaster> updateDeal(@PathVariable Integer id,@Valid @RequestBody GoNoGoMaster dealDetails) {
        GoNoGoMaster updatedDeal = dealService.updateDeal(id, dealDetails);
        return updatedDeal != null ? new ResponseEntity<>(updatedDeal, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


//    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable Integer id) {
        boolean isDeleted = dealService.deleteDeal(id);
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
