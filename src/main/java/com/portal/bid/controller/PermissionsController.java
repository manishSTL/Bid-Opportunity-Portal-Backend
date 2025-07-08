package com.portal.bid.controller;

import com.portal.bid.entity.Permissions;
import com.portal.bid.service.PermissionsService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
//@CrossOrigin(origins = "${app.cors.allowed-origins}")  // Move CORS to configuration
public class PermissionsController {

    private final PermissionsService permissionsService;

    @Autowired
    public PermissionsController(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @GetMapping
    public ResponseEntity<List<Permissions>> getAllPermissions() {
        List<Permissions> permissions = permissionsService.getAllPermissions();
        return permissions.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permissions> getPermissionById(@PathVariable int id) {
        return permissionsService.getPermissionById(id)
                .map(permission -> new ResponseEntity<>(permission, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Permissions> createPermission(@Valid @RequestBody Permissions permission) {
        try {
            Permissions createdPermission = permissionsService.createPermission(permission);
            return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permissions> updatePermission(
            @PathVariable int id,
            @Valid @RequestBody Permissions permission) {
        try {
            Optional<Permissions> existingPermissionOpt = permissionsService.getPermissionById(id);

            if (existingPermissionOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Permissions existingPermission = existingPermissionOpt.get();

            // Update fields using a utility method
            updatePermissionFields(existingPermission, permission);

            Permissions updatedPermission = permissionsService.createPermission(existingPermission);
            return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePermission(@PathVariable int id) {
        try {
            boolean deleted = permissionsService.deletePermission(id);
            Map<String, String> response = new HashMap<>();

            if (deleted) {
                response.put("message", "Permission successfully deleted");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Permission not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to delete permission");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    // Custom exception handler for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralExceptions(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "An unexpected error occurred");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void updatePermissionFields(Permissions existingPermission, Permissions newPermission) {
        if (newPermission.getPermission_name() != null) {
            existingPermission.setPermission_name(newPermission.getPermission_name());
        }
        if (newPermission.getCreatedBy() != null) {
            existingPermission.setCreatedBy(newPermission.getCreatedBy());
        }
        if (newPermission.getUpdatedBy() != null) {
            existingPermission.setUpdatedBy(newPermission.getUpdatedBy());
        }
    }
}