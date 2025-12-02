package com.moustass.model;

import java.time.LocalDateTime;

public class UploadedFile {
    private Integer id;
    private Integer userId;
    private String fileName;
    private String fileHash;
    private LocalDateTime uploadedAt;

    public UploadedFile() {
    }

    public UploadedFile(Integer id, Integer userId, String fileName, String fileHash, LocalDateTime uploadedAt) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
        this.fileHash = fileHash;
        this.uploadedAt = uploadedAt;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
