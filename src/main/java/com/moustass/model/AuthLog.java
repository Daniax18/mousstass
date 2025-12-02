package com.moustass.model;

import java.time.LocalDateTime;

public class AuthLog {
    private Integer id;
    private Integer userId;
    private AuthEvent event;
    private String ipAddress;
    private LocalDateTime createdAt;

    public AuthLog() {
    }

    public AuthLog(Integer id, Integer userId, AuthEvent event, String ipAddress, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.event = event;
        this.ipAddress = ipAddress;
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

    public AuthEvent getEvent() {
        return event;
    }

    public void setEvent(AuthEvent event) {
        this.event = event;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
