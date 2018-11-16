package com.oose.group6.backend.beans.model;

import java.io.Serializable;
import java.sql.Date;

public class Insurance implements Serializable {

    // insurance
    private String policyId;
    private String company;
    private Date effectiveDate;
    private Date endDate;

    public Insurance() {
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
