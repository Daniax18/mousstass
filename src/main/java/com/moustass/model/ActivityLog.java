package com.moustass.model;

import java.time.LocalDateTime;

public class ActivityLog {
    private Integer id;
    private Integer userId;
    private TypeAction action;
    private String details;
    private LocalDateTime createdAt;

    public enum TypeAction{
        LOGIN,
        LOGOUT,
        FILE_UPLOAD,
        FILE_DOWNLOAD,
        USER_CREATED
    }

    public ActivityLog() {
    }

    public ActivityLog(Integer userId, TypeAction action, String details) {
        this.userId = userId;
        this.action = action;
        this.details = details;
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

    public TypeAction getAction() {
        return action;
    }

    public void setAction(TypeAction action) {
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
