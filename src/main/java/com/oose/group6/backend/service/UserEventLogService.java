package com.oose.group6.backend.service;

import com.oose.group6.backend.persistence.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserEventLogService {
    void logUserEvent(HttpServletRequest request, UserEntity user, String event);
}
