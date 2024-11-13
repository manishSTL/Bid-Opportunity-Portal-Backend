package com.portal.bid.controller;


import com.portal.bid.entity.GoNoGoStatus;
import com.portal.bid.entity.PlanAction;
import com.portal.bid.service.GoNoGoStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gonogostatus")
public class GoNoGoStatusController {

    @Autowired
    private GoNoGoStatusService goNoGoStatusService;
//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    ResponseEntity<GoNoGoStatus> createStatus(@Valid @RequestBody GoNoGoStatus entry){
        GoNoGoStatus createdEntry  = goNoGoStatusService.createEntry(entry);
        return ResponseEntity.ok(createdEntry);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    ResponseEntity<GoNoGoStatus> updateStatus(@PathVariable  Long id ,@Valid @RequestBody GoNoGoStatus entry){
        entry.setCreatedAt(LocalDateTime.now());
//        if(entry.getCreatedBy()==null ){
//            return ResponseEntity.badRequest().build();
//        }
        GoNoGoStatus updatedEntry  = goNoGoStatusService.updateEntry(entry,id);
        return  updatedEntry!=null?ResponseEntity.ok(updatedEntry):ResponseEntity.badRequest().build();
    }
//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    ResponseEntity<GoNoGoStatus>  getStatusById(@PathVariable Long id){
        GoNoGoStatus entryFound = goNoGoStatusService.findbyID(id);
        return entryFound!=null?ResponseEntity.ok(entryFound):ResponseEntity.notFound().build();
    }
//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    ResponseEntity<List<GoNoGoStatus>>  getAll(@RequestParam(required = false) Long form_id){
        List<GoNoGoStatus> entryFound;
        if (form_id != null) {
            entryFound = goNoGoStatusService.getAllGoNoGoStatus(form_id);
        } else {
            entryFound = goNoGoStatusService.findAll();
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
