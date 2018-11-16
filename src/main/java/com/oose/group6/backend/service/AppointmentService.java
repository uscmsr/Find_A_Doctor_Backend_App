package com.oose.group6.backend.service;

import com.oose.group6.backend.beans.model.Appointment;
import com.oose.group6.backend.persistence.AppointmentEntity;
import com.oose.group6.backend.persistence.UserEntity;

import java.util.List;

public interface AppointmentService {
    Appointment getApptDetail(UserEntity user, String apptId) throws AppointmentServiceException;

    AppointmentEntity scheduleAppt(UserEntity user, Appointment appointment) throws AppointmentServiceException;

    List<Appointment> getPastApptByUser(UserEntity user) throws AppointmentServiceException;
    List<Appointment> getFutureApptByUser(UserEntity user) throws AppointmentServiceException;

    /**
     * Exception for Account Service
     */
    class AppointmentServiceException extends Exception {

        public AppointmentServiceException(String message) {
            super(message);
        }
    }
}
