package com.moustass.model;

import java.time.LocalDateTime;

/**
 * Represents a digital signature entry.
 * <p>
 * This class stores information related to file signing operations,
 * including the file hash and its associated digital signature,
 * in order to ensure integrity and traceability.
 * </p>
 */

public class SignatureLog {
    private Integer id;
    private Integer userId;
    private String fileName;
    private String fileHash;
    private String signatureValue;
    private LocalDateTime createdAt;

    public SignatureLog() {
    }

    /**
     * Creates a new signature log entry.
     *
     * @param userId         the identifier of the user who signed the file
     * @param fileName       the name of the signed file
     * @param fileHash       the cryptographic hash of the file
     * @param signatureValue the digital signature associated with the file
     * @param createdAt      the date and time of the signing operation
     */

    public SignatureLog(Integer userId, String fileName, String fileHash, String signatureValue, LocalDateTime createdAt) {
        this.userId = userId;
        this.fileName = fileName;
        this.fileHash = fileHash;
        this.signatureValue = signatureValue;
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

    public String getSignatureValue() {
        return signatureValue;
    }

    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
