package com.oose.group6.backend.controller.packet.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Schedule new appointment request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class ScheduleApptReq {
    @NotNull
    private String StaffUsername;

    @NotNull
    private Timestamp startTime;

    @NotNull
    private Timestamp endTime;

    private String location;
    private String symptom;

    private String allergies;

    public ScheduleApptReq() {
    }

    public String getStaffUsername() {
        return StaffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        StaffUsername = staffUsername;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

}