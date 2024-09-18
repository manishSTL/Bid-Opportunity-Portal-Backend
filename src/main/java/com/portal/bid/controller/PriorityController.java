package com.portal.bid.controller;

import com.portal.bid.entity.Priority;
import com.portal.bid.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/priority")
public class PriorityController {

    @Autowired
    private PriorityService service;

//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Priority> createPriority(@RequestBody Priority priority) {
        priority.setCreatedAt(LocalDateTime.now());
        Priority createdPriority = service.save(priority);
        return new ResponseEntity<>(createdPriority, HttpStatus.CREATED);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<Priority> updatePriority(@PathVariable Integer id, @RequestBody Priority priority) {
        priority.setUpdatedAt(LocalDateTime.now());
        if (priority.getUpdatedBy() == null || priority.getPriority() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return 400 Bad Request if validation fails
        }
        Optional<Priority> existingPriority = service.findById(id);
        if (existingPriority.isPresent()) {
            priority.setId(id);
            Priority updatedPriority = service.save(priority);
            return new ResponseEntity<>(updatedPriority, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 Not Found if the entity doesn't exist
        }
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<Priority> getPriorityById(@PathVariable Integer id) {
        Optional<Priority> priority = service.findById(id);
        return priority.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriority(@PathVariable Integer id) {
        Optional<Priority> priority = service.findById(id);
        if (priority.isPresent()) {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 Not Found if the entity doesn't exist
        }
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<Priority>> getAllPriorities() {
        List<Priority> priorities = service.findAll();
        return new ResponseEntity<>(priorities, HttpStatus.OK);
    }
}
