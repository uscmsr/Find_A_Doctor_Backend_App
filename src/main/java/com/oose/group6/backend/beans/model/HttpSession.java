package com.oose.group6.backend.beans.model;

public class HttpSession {

    private long userId;

    private String sessionId;

    public HttpSession(long userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
