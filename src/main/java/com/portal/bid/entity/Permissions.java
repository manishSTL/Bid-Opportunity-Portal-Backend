package com.portal.bid.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "permissions")
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @NotBlank(message = "Permission name cannot be blank")
    @Size(min = 3, max = 50, message = "Permission name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Permission name can only contain letters, numbers, dots, hyphens, and underscores")
    @Column(name = "permission_name")
    private String permissionName;

//    @NotNull(message = "Created date cannot be null")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    @NotBlank(message = "Created by cannot be blank")
    @Size(max = 50, message = "Created by must not exceed 50 characters")
    @Column(name = "created_by", updatable = false)
    private String createdBy;

//    @NotNull(message = "Updated date cannot be null")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @NotBlank(message = "Updated by cannot be blank")
    @Size(max = 50, message = "Updated by must not exceed 50 characters")
    @Column(name = "update_by")
    private String updatedBy;

    @PrePersist
    private void onCreation() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdation() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters remain the same
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermission_name() {
        return permissionName;
    }

    public void setPermission_name(String permission_name) {
        this.permissionName = permission_name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}