package com.oose.group6.backend.controller.packet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.beans.model.Appointment;
import com.oose.group6.backend.beans.model.profile.BaseProfile;

import java.util.List;

/**
 * Response containing list of appointments
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class FindDoctorRsp extends BaseRsp {
    private List<BaseProfile> doctorList;

    public FindDoctorRsp() { }

    public FindDoctorRsp(List<BaseProfile> doctorList) {
        this.doctorList = doctorList;
    }

    public List<BaseProfile> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<BaseProfile> doctorList) {
        this.doctorList = doctorList;
    }

}