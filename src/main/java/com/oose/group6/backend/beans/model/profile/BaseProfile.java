package com.oose.group6.backend.beans.model.profile;

import java.io.Serializable;

/**
 * Minimum Profile used for simple list results
 * Do not add other common fields of profiles to this class
 */
public class BaseProfile implements Serializable {

    private String name;

    // http unique
    private String username;

    public BaseProfile() {}

    public BaseProfile(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
