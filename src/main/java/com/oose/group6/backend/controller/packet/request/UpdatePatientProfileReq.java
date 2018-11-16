package com.oose.group6.backend.controller.packet.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.common.FusionConfig;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class UpdatePatientProfileReq {

    @NotNull
    @Size(min = FusionConfig.NAME_MIN_LENGTH, max = FusionConfig.NAME_MAX_LENGTH)
    private String name;
    private String address;
    private String race;
    private Date dob;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String communicationPref;
    private String medicalPref;
    private String staffPref;
    private String email;
    private String phone;
    @NotNull
    private String gender;
    private String photoUrl;

    // HTTP unique
    private String pcpUsername;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getCommunicationPref() {
        return communicationPref;
    }

    public void setCommunicationPref(String communicationPref) {
        this.communicationPref = communicationPref;
    }

    public String getMedicalPref() {
        return medicalPref;
    }

    public void setMedicalPref(String medicalPref) {
        this.medicalPref = medicalPref;
    }

    public String getStaffPref() {
        return staffPref;
    }

    public void setStaffPref(String staffPref) {
        this.staffPref = staffPref;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPcpUsername() {
        return pcpUsername;
    }

    public void setPcpUsername(String pcpUsername) {
        this.pcpUsername = pcpUsername;
    }


}