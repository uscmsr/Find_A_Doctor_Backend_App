package com.oose.group6.backend.controller.packet.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Login request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class FindDoctorReq {

    private String symptom;

    public FindDoctorReq() {

    }

    public FindDoctorReq(String symptom) {
        this.symptom = symptom;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

}