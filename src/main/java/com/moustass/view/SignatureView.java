package com.moustass.view;

import java.time.LocalDateTime;

public class SignatureView {
    private Integer idSignature;
    private String userName;
    private String fileName;
    private LocalDateTime dateSignature;

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
