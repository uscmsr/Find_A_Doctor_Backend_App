package com.oose.group6.backend.beans.manager;


import com.oose.group6.backend.beans.model.HttpSession;

/**
 * HttpSession manager behaviors
 */
public interface SessionManager {

    HttpSession createSession(long userId);

    /**
     * Get session by id, returns null if not found
     */
    HttpSession getSession(String sessionId);

    void deleteSession(String sessionId);

}
