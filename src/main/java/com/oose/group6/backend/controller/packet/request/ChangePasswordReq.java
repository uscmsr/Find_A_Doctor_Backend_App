package com.oose.group6.backend.controller.packet.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oose.group6.backend.common.FusionConfig;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class ChangePasswordReq {

    @NotNull
    @Size(min = FusionConfig.PASSWORD_MIN_LENGTH, max = FusionConfig.PASSWORD_MAX_LENGTH)
    private String oldPassword;

    @NotNull
    @Size(min = FusionConfig.PASSWORD_MIN_LENGTH, max = FusionConfig.PASSWORD_MAX_LENGTH)
    private String newPassword;

    public ChangePasswordReq() {
    }

    public ChangePasswordReq(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}