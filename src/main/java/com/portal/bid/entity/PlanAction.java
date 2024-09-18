package com.portal.bid.entity;


import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan")
public class PlanAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("form_id")
    @Column(name = "form_id")
    private Long formId;

    @JsonProperty("date")
    @Column(name = "date")
    private LocalDateTime date;

    @JsonProperty("week")
    @Column(name = "week")
    private Integer week;

    @JsonProperty("plan")
    @Column(name = "plan")
    private String plan;

    @JsonProperty("action")
    @Column(name = "action")
    private String action;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    @Column(name = "created_by")
    private String createdBy;

    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("updated_by")
    @Column(name = "updated_by")
    private String updatedBy;

    //to calculate week from the date
//    @PrePersist
//    @PreUpdate
//    public void calculateWeek() {
//        this.week = (date != null) ? date.get(WeekFields.of(Locale.getDefault()).weekOfYear()) : null;
//    }
    // to calculate week from the date
    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.week = (date != null) ? date.get(WeekFields.of(Locale.getDefault()).weekOfYear()) : null;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.week = (date != null) ? date.get(WeekFields.of(Locale.getDefault()).weekOfYear()) : null;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
