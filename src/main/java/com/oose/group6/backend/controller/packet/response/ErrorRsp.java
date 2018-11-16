package com.oose.group6.backend.controller.packet.response;

/**
 * Error Response
 */
public class ErrorRsp extends BaseRsp {

    public ErrorRsp(String message) {
        setMessage(message);
    }
}
