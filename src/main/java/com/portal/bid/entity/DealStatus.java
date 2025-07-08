package com.portal.bid.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "deal_status")
public class DealStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("form_id")
    @Column(name = "form_id", nullable = false)
    private Long form_id;

    // @Size(min = 3, max = 30, message = "Deal status must be between 3 and 50 characters")
    // @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Deal status can only contain letters, numbers, dots, hyphens, and underscores")
    // @JsonProperty("deal_status")
    // @Column(name = "deal_status", nullable = false)
    // private String deal_status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_status")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Deal dealStatus;


    @JsonProperty("created_at")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public Long getForm_id() {
        return form_id;
    }

    public void setForm_id(Long form_id) {
        this.form_id = form_id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Deal getDealStatus() {
        return dealStatus;
    }
    
    public void setDealStatus(Deal dealStatus) {
        this.dealStatus = dealStatus;
    }
    

    
}
