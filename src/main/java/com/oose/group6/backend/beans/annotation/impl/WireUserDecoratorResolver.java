package com.oose.group6.backend.beans.annotation.impl;

import com.oose.group6.backend.beans.annotation.WireUser;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.persistence.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class WireUserDecoratorResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserDao userDao;

    /**
     * When is this supported
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterType().isAssignableFrom(UserEntity.class) &&
                parameter.hasParameterAnnotation(WireUser.class));
    }

    /**
     * Return UserEntity by parsing userId.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            Long userId = (Long) webRequest.getAttribute(
            FusionConfig.HEADER_FIELD_USER_ID, RequestAttributes.SCOPE_REQUEST);
            return userDao.findById(userId);
        } catch (Exception e) {
            return null;
        }
    }
}
