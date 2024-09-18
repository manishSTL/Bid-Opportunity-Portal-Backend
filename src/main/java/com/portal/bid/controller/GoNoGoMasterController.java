package com.portal.bid.controller;


import com.portal.bid.entity.GoNoGoMaster;
import com.portal.bid.service.DealService;
import com.portal.bid.service.GoNoGoMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/go-no-go")
public class GoNoGoMasterController {

    @Autowired
    private GoNoGoMasterService dealService;

//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<GoNoGoMaster> createDeal(@RequestBody GoNoGoMaster deal) {
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
    public ResponseEntity<GoNoGoMaster> updateDeal(@PathVariable Integer id, @RequestBody GoNoGoMaster dealDetails) {
        GoNoGoMaster updatedDeal = dealService.updateDeal(id, dealDetails);
        return updatedDeal != null ? new ResponseEntity<>(updatedDeal, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


//    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable Integer id) {
        boolean isDeleted = dealService.deleteDeal(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
