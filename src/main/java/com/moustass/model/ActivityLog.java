package com.moustass.model;

import java.time.LocalDateTime;

/**
 * Represents an activity log entry.
 * <p>
 * This class is used to record user actions within the application
 * in order to ensure traceability, auditing, and security monitoring.
 * </p>
 */
public class ActivityLog {
    private Integer id;
    private Integer userId;
    private TypeAction action;
    private String details;
    private LocalDateTime createdAt;

    /**
     * Enumeration of possible user actions recorded in the activity log.
     * <p>
     * This enum defines all actions that can be audited within the system,
     * such as authentication events and file operations.
     * </p>
     */
    public enum TypeAction{
        LOGIN,
        LOGOUT,
        FILE_UPLOAD,
        FILE_DOWNLOAD,
        USER_CREATED,
        LOGIN_FAILURE,
        LOGIN_SUCCESS
    }

    public ActivityLog() {
    }

    /**
     * Creates a new activity log entry.
     *
     * @param userId  the identifier of the user who performed the action
     * @param action  the type of action performed
     * @param details additional information describing the action
     */

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
