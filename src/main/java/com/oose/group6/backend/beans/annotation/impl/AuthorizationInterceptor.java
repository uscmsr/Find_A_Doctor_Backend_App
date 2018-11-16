package com.oose.group6.backend.beans.annotation.impl;

import com.oose.group6.backend.beans.annotation.LoginRequired;
import com.oose.group6.backend.beans.manager.SessionManager;
import com.oose.group6.backend.beans.model.HttpSession;
import com.oose.group6.backend.common.FusionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Interceptor for session checking
 * Append userId in the request header,
 * this field will be set to null if user is not authorized
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.getAnnotation(LoginRequired.class) == null) {
            return true;
        } else {
            String id = request.getHeader(FusionConfig.HEADER_FIELD_SESSION_ID);   // returns null if not found
            HttpSession session;
            try {
                session = sessionManager.getSession(id);
            } catch (Exception e) { session  = null; }
            if (session != null) {
                request.setAttribute(FusionConfig.HEADER_FIELD_USER_ID, session.getUserId());
                return true;
            } else {
                // attach empty user id -> unauthorized
                request.setAttribute(FusionConfig.HEADER_FIELD_USER_ID, "");
            }
        return true;
        }
    }
}
