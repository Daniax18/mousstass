package com.moustass.model;

import java.time.LocalDateTime;

public class ActivityLog {
    private Integer id;
    private Integer userId;
    private String action;
    private String details;
    private LocalDateTime createdAt;

    public ActivityLog() {
    }

    public ActivityLog(Integer id, Integer userId, String action, String details, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
