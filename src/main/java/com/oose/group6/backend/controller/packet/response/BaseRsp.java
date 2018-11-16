package com.oose.group6.backend.controller.packet.response;

/**
 * Base Response Class
 */
public class BaseRsp {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message = "OK";
}
