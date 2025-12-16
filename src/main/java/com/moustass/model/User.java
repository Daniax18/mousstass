package com.moustass.model;

import java.time.LocalDateTime;

public class User {
    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private String passwordHash;
    private String salt;
    private String pkPublic;
    private String skPrivate;
    private Boolean mustChangePwd;
    private Boolean isAdmin;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Integer id, String firstname, String lastname, String username, String passwordHash, String salt, String pkPublic, String skPrivate, Boolean mustChangePwd, Boolean isAdmin, LocalDateTime createdAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.pkPublic = pkPublic;
        this.skPrivate = skPrivate;
        this.mustChangePwd = mustChangePwd;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPkPublic() {
        return pkPublic;
    }

    public void setPkPublic(String pkPublic) {
        this.pkPublic = pkPublic;
    }

    public String getSkPrivate() {
        return skPrivate;
    }

    public void setSkPrivate(String skPrivate) {
        this.skPrivate = skPrivate;
    }

    public Boolean getMustChangePwd() {
        return mustChangePwd;
    }

    public void setMustChangePwd(Boolean mustChangePwd) {
        this.mustChangePwd = mustChangePwd;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
