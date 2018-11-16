package com.oose.group6.backend.controller.packet.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.beans.model.Department;
import com.oose.group6.backend.beans.model.MedicalCenter;
import com.oose.group6.backend.common.FusionConfig;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class UpdateStaffProfileReq {

    @NotNull
    @Size(min = FusionConfig.NAME_MIN_LENGTH, max = FusionConfig.NAME_MAX_LENGTH)
    private String name;
    private String speciality;
    private String email;
    private String phone;
    @NotNull
    private String gender;
    private String photoUrl;

    // HTTP unique
    @NotNull
    private Long departmentId;
    @NotNull
    private Long medicalCenterId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getMedicalCenterId() {
        return medicalCenterId;
    }

    public void setMedicalCenterId(Long medicalCenterId) {
        this.medicalCenterId = medicalCenterId;
    }
}