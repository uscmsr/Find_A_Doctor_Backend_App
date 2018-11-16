package com.oose.group6.backend.controller.packet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.beans.model.profile.BaseProfile;

import java.util.List;

/**
 * PatientProfile Response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class StaffPcpRsp extends BaseRsp {

    private List<BaseProfile> pcpList;


    public StaffPcpRsp() { }

    public StaffPcpRsp(List<BaseProfile> pcpList) {
        this.pcpList = pcpList;
    }

    public List<BaseProfile> getPcpList() {
        return pcpList;
    }

    public void setPcpList(List<BaseProfile> pcpList) {
        this.pcpList = pcpList;
    }

}