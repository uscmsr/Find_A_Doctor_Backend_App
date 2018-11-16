package com.oose.group6.backend.service;

import com.oose.group6.backend.persistence.UserEntity;

import java.sql.Timestamp;

public interface AccountService {
    UserEntity verifyLogin(String username, String password) throws AccountServiceException;

    void logoutSession(String sessionId) throws AccountServiceException;

    void createNewUser(String username, String password, String userType, String name)
            throws AccountServiceException;

    Timestamp recordLoginTime(UserEntity userEntity) throws AccountServiceException;

    void changePassword(UserEntity user, String oldPass, String newPass) throws AccountServiceException;

    /**
     * Exception for Account Service
     */
    class AccountServiceException extends Exception {

        public AccountServiceException(String message) {
            super(message);
        }
    }
}
