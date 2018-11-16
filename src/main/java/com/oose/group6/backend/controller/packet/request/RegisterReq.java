package com.oose.group6.backend.controller.packet.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.common.FusionConfig;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * New user registration request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class RegisterReq {
    @NotNull
    @Size(min = FusionConfig.USERNAME_MIN_LENGTH, max = FusionConfig.USERNAME_MAX_LENGTH)
    private String username;

    @NotNull
    @Size(min = FusionConfig.PASSWORD_MIN_LENGTH, max = FusionConfig.PASSWORD_MAX_LENGTH)
    private String password;

    @NotNull
    private String userType;

    @NotNull
    private String name;

    public RegisterReq() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}