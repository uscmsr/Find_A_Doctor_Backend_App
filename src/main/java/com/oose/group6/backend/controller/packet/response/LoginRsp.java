package com.oose.group6.backend.controller.packet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.sql.Timestamp;

/**
 * Login Response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class LoginRsp extends BaseRsp {
    private String sessionId;
    private String userType;
    private Timestamp lastLogin;
    public LoginRsp() { }

    public LoginRsp(String sessionId, String userType, Timestamp lastLogin) {
        this.sessionId = sessionId;
        this.userType = userType;
        this.lastLogin = lastLogin;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

}