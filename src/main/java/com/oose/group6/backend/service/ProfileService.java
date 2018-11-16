package com.oose.group6.backend.service;

import com.oose.group6.backend.beans.model.Department;
import com.oose.group6.backend.beans.model.Insurance;
import com.oose.group6.backend.beans.model.Language;
import com.oose.group6.backend.beans.model.MedicalCenter;
import com.oose.group6.backend.beans.model.profile.BaseProfile;
import com.oose.group6.backend.beans.model.profile.PatientProfile;
import com.oose.group6.backend.beans.model.profile.StaffProfile;
import com.oose.group6.backend.controller.packet.request.UpdatePatientProfileReq;
import com.oose.group6.backend.controller.packet.request.UpdateStaffProfileReq;
import com.oose.group6.backend.persistence.UserEntity;

import java.util.List;

public interface ProfileService {
    PatientProfile getPatientProfile(UserEntity user) throws ProfileServiceException;

    StaffProfile getStaffProfile(String username) throws ProfileServiceException;

    void updatePatientProfile(UserEntity user, UpdatePatientProfileReq updatePatientProfileReq)
            throws ProfileServiceException;

    void updateStaffProfile(UserEntity user, UpdateStaffProfileReq updateStaffProfileReq)
                    throws ProfileServiceException;

    List<BaseProfile> getStaffPcp(UserEntity userEntity) throws ProfileServiceException;

    Insurance getPatientInsurance(UserEntity user) throws ProfileServiceException;

    List<Language> getUserLanguageList(UserEntity user) throws ProfileServiceException;

    List<Language> getUserLanguageList(String username) throws ProfileServiceException;

    List<MedicalCenter> getMedicalCenterList() throws ProfileServiceException;

    List<Department> getDepartmentList() throws ProfileServiceException;

        /**
         * Exception for Profile Service
         */
    class ProfileServiceException extends Exception {

        public ProfileServiceException(String message) {
            super(message);
        }
    }
}
