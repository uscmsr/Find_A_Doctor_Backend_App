package com.oose.group6.backend.service.impl;

import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.persistence.UserEventLogEntity;
import com.oose.group6.backend.persistence.dao.UserEventLogDao;
import com.oose.group6.backend.service.UserEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
public class UserEventLogServiceImpl implements UserEventLogService {

    private UserEventLogDao userEventLogDao;

    @Autowired
    public UserEventLogServiceImpl(UserEventLogDao userEventLogDao) {
        this.userEventLogDao = userEventLogDao;
    }

    @Override
    public void logUserEvent(HttpServletRequest request, UserEntity user, String event) {
        UserEventLogEntity logEntity = new UserEventLogEntity();
        logEntity.setTimestamp(new Timestamp(System.currentTimeMillis()));
        logEntity.setUser(user);
        logEntity.setOperation(event);

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        System.out.println(ipAddress);
        ipAddress = ipAddress.contains(",") ? ipAddress.split(",")[0] : ipAddress;
        logEntity.setIp(ipAddress);

        try {
            String ua = request.getHeader("User-Agent");
            if (ua.length() > FusionConfig.USER_EVENT_LOG_UA_FIELD_MAX_LENGTH) {
                ua = ua.substring(0,FusionConfig.USER_EVENT_LOG_UA_FIELD_MAX_LENGTH);
            }
            logEntity.setAgent(ua);
        } catch (Exception e) {
            logEntity.setAgent(e.toString());
        }

        userEventLogDao.save(logEntity);
    }

}
