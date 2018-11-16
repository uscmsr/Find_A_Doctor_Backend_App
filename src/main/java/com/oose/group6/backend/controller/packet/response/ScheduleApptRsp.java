package com.oose.group6.backend.controller.packet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Schedule new appointment request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class ScheduleApptRsp extends BaseRsp {
    private Long id;

    public ScheduleApptRsp(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}