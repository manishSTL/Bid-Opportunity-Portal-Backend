package com.portal.bid.controller;

import com.portal.bid.entity.UserRoles;
import com.portal.bid.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-roles")
public class UserRolesController {

    @Autowired
    private UserRolesService userRolesService;

    //    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<UserRoles> create(@RequestBody UserRoles userroles) {
        System.out.println("in response entiy");
        UserRoles u = userRolesService.save(userroles);
        return ResponseEntity.ok(u);
    }

    //    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<UserRoles> update(@PathVariable int id, @RequestBody UserRoles userroles) {
        Optional<UserRoles> userRole = userRolesService.updateUser(id, userroles);
        return userRole.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<UserRoles> get(@PathVariable int id) {
        Optional<UserRoles> userRole = userRolesService.get(id);
        if (userRole.isPresent()) {
            return ResponseEntity.ok(userRole.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<UserRoles> userRole = userRolesService.get(id);
        if (userRole.isPresent()) {
            userRolesService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<UserRoles>> getAll() {
        List<UserRoles> userRoles = userRolesService.getAll();
        return ResponseEntity.ok(userRoles);
    }
}