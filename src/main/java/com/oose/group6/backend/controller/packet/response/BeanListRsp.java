package com.oose.group6.backend.controller.packet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.beans.model.Appointment;
import com.oose.group6.backend.beans.model.Department;

import java.io.Serializable;
import java.util.List;

/**
 * Response containing list of beans
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class BeanListRsp<T extends Serializable> extends BaseRsp {
    private List<T> resultList;

    public BeanListRsp() { }

    public BeanListRsp(List<T> resultList) {
        this.resultList = resultList;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}