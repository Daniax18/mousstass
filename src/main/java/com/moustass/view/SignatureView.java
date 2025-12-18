package com.moustass.view;

import java.time.LocalDateTime;

/**
 * Represents a read-only view of all file signed.
 * <p>
 * This class is used to expose signature-related information for display
 * or reporting purposes, without exposing sensitive cryptographic data.
 * </p>
 */
public class SignatureView {
    private Integer idSignature;
    private String userName;
    private String fileName;
    private LocalDateTime dateSignature;

    /**
     * Creates a new signature view instance.
     *
     * @param idSignature  the identifier of the signature
     * @param userName     the username of the signer
     * @param fileName     the name of the signed file
     * @param dateSignature the date and time of the signature
     */
    public SignatureView(Integer idSignature, String userName, String fileName, LocalDateTime dateSignature) {
        this.idSignature = idSignature;
        this.userName = userName;
        this.fileName = fileName;
        this.dateSignature = dateSignature;
    }

    public SignatureView() {
    }

    public Integer getIdSignature() {
        return idSignature;
    }

    public void setIdSignature(Integer idSignature) {
        this.idSignature = idSignature;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(LocalDateTime dateSignature) {
        this.dateSignature = dateSignature;
    }
}
