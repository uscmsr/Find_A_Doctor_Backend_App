package com.oose.group6.backend.controller.packet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.beans.model.Language;
import com.oose.group6.backend.beans.model.profile.StaffProfile;

import java.util.List;

/**
 * PatientProfile Response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class StaffProfileRsp extends BaseRsp {
    private StaffProfile staffProfile;
    private List<Language> languages;


    public StaffProfileRsp() { }

    public StaffProfileRsp(StaffProfile staffProfile, List<Language> languages) {
        this.staffProfile = staffProfile;
        this.languages = languages;
    }

    public StaffProfile getStaffProfile() {
        return staffProfile;
    }

    public void setStaffProfile(StaffProfile staffProfile) {
        this.staffProfile = staffProfile;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

}