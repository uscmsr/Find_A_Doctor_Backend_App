package com.oose.group6.backend.service.impl;


import com.oose.group6.backend.beans.manager.SessionManager;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.persistence.MedicalStaffEntity;
import com.oose.group6.backend.persistence.PatientEntity;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.persistence.dao.MedicalStaffDao;
import com.oose.group6.backend.persistence.dao.PatientDao;
import com.oose.group6.backend.persistence.dao.UserDao;
import com.oose.group6.backend.service.AccountService;
import com.oose.group6.backend.service.AppointmentService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Provide login and registration service
 */
@Service
public class AccountServiceImpl implements AccountService {

    private UserDao userDao;
    private PatientDao patientDao;
    private MedicalStaffDao medicalStaffDao;
    private SessionManager sessionManager;
    private SessionFactory sessionFactory;

    @Autowired
    public AccountServiceImpl(UserDao userDao, PatientDao patientDao, MedicalStaffDao medicalStaffDao,
                          SessionManager sessionManager, SessionFactory sessionFactory) {
        this.userDao = userDao;
        this.patientDao = patientDao;
        this.medicalStaffDao = medicalStaffDao;
        this.sessionManager = sessionManager;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Login
     */
    @Override
    public UserEntity verifyLogin(String username, String password) throws AccountServiceException {
        UserEntity user = userDao.findByUsername(username);
        if (user == null) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_INVALID_USERNAME);
        }
        if (! user.getPassword().equals(password)) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_INVALID_PASSWORD);
        }
        return user;
    }

    /**
     * Logout.
     * Always returns OK not matter given session is valid or not.
     * @throws AccountServiceException SessionId is missing
     */
    @Override
    public void logoutSession(String sessionId) throws AccountServiceException {
        if (sessionId == null) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_SESSION_HEADER_MISSING);
        }
        try {
            sessionManager.deleteSession(sessionId);
        } catch (Exception e) {}
    }

    @Override
    public Timestamp recordLoginTime(UserEntity userEntity) throws AccountServiceException {
        if (userEntity == null) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR);
        }
        Timestamp last = userEntity.getLastLogin();
        userEntity.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userDao.save(userEntity);
        return last;
    }

    /**
     * Create new user
     */
    @Override
    public void createNewUser(String username, String password, String userType, String name)
            throws AccountServiceException {
        // check userType
        userType = userType.toUpperCase();
        if (! FusionConfig.UserRole.getAll().contains(userType)) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_USER_ROLE_ERROR);
        }
        // check username
        if (userDao.findByUsername(username) != null) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_USERNAME_ALREADY_EXISTS);
        }
        // create user
        UserEntity userEntity = new UserEntity();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userEntity.setRegistrationDate(timestamp);
        userEntity.setLastLogin(timestamp);
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRole(userType);
        userDao.save(userEntity);
        userEntity = userDao.findByUsername(username);
        // map new staff of patient
        if (userType.equals(FusionConfig.UserRole.STAFF)) {
            createNewStaff(userEntity, name);
        }
        if (userType.equals(FusionConfig.UserRole.PATIENT)) {
            createNewPatient(userEntity, name);
        }
    }

    @Override
    public void changePassword(UserEntity user, String oldPass, String newPass) throws AccountServiceException {
        if (oldPass == null || newPass == null) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_MALFORMED_JSON);
        } else if (user == null) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR);
        } else if (! user.getPassword().equals(oldPass)) {
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_INVALID_PASSWORD);
        }
        // update
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.evict(user);
            user.setPassword(newPass);
            session.merge(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new AccountServiceException(FusionConfig.RESPONSE_JSON_MSG_SERVER_BUSY);
        } finally {
            session.close();
        }
    }

    private void createNewStaff(UserEntity user, String name) {
        MedicalStaffEntity entity = new MedicalStaffEntity();
        entity.setUser(user);
        entity.setName(name);
        medicalStaffDao.save(entity);
    }

    private void createNewPatient(UserEntity user, String name) {
        PatientEntity entity = new PatientEntity();
        entity.setUser(user);
        entity.setName(name);
        patientDao.save(entity);
    }

}
