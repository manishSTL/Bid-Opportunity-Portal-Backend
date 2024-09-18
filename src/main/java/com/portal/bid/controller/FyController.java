package com.portal.bid.controller;

import com.portal.bid.entity.Fy;
import com.portal.bid.service.FyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fy")
public class FyController {

    @Autowired
    private FyService fyService;
//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<Fy> getAllFys() {
        return fyService.findAll();
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<Fy> getFyById(@PathVariable int id) {
        Optional<Fy> fy = fyService.findById(id);
        return fy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public Fy createFy(@RequestBody Fy fy) {
        return fyService.save(fy);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<Fy> updateFy(@PathVariable int id, @RequestBody Fy fyDetails) {
        Optional<Fy> fyOptional = fyService.findById(id);
        if (fyOptional.isPresent()) {
            Fy fy = fyOptional.get();
            fy.setObFy(fyDetails.getObFy());
            fy.setCreatedBy(fyDetails.getCreatedBy());
            fy.setUpdatedBy(fyDetails.getUpdatedBy());
            fy.setUpdatedAt(LocalDateTime.now());
            final Fy updatedFy = fyService.save(fy);
            return ResponseEntity.ok(updatedFy);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFy(@PathVariable int id) {
        Optional<Fy> fyOptional = fyService.findById(id);
        if (fyOptional.isPresent()) {
            fyService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
