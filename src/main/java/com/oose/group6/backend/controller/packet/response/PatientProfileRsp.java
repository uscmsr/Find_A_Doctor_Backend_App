package com.oose.group6.backend.controller.packet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.beans.model.Insurance;
import com.oose.group6.backend.beans.model.Language;
import com.oose.group6.backend.beans.model.profile.PatientProfile;

import java.util.List;

/**
 * PatientProfile Response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class PatientProfileRsp extends BaseRsp {
    private PatientProfile patientProfile;
    private Insurance insurance;
    private List<Language> languages;


    public PatientProfileRsp() { }

    public PatientProfileRsp(PatientProfile patientProfile, Insurance insurance, List<Language> languages) {
        this.patientProfile = patientProfile;
        this.insurance = insurance;
        this.languages = languages;
    }


    public PatientProfile getPatientProfile() {
        return patientProfile;
    }

    public void setPatientProfile(PatientProfile patientProfile) {
        this.patientProfile = patientProfile;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
}