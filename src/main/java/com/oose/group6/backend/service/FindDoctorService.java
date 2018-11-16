package com.oose.group6.backend.service;

import com.oose.group6.backend.beans.model.profile.BaseProfile;

import java.util.List;

public interface FindDoctorService {
    List<BaseProfile> getDoctorList(String input) throws FindDoctorServiceException;

    class FindDoctorServiceException extends Exception {
        public FindDoctorServiceException(String message) {
            super(message);
        }
    }
}
