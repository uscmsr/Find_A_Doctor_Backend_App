package com.oose.group6.backend.controller;

import com.oose.group6.backend.beans.annotation.LoginRequired;
import com.oose.group6.backend.beans.annotation.WireUser;
import com.oose.group6.backend.beans.model.Department;
import com.oose.group6.backend.beans.model.Insurance;
import com.oose.group6.backend.beans.model.Language;
import com.oose.group6.backend.beans.model.MedicalCenter;
import com.oose.group6.backend.beans.model.profile.BaseProfile;
import com.oose.group6.backend.beans.model.profile.PatientProfile;
import com.oose.group6.backend.beans.model.profile.StaffProfile;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.controller.packet.request.UpdatePatientProfileReq;
import com.oose.group6.backend.controller.packet.request.UpdateStaffProfileReq;
import com.oose.group6.backend.controller.packet.response.*;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.service.ProfileService;
import com.oose.group6.backend.service.UserEventLogService;
import groovy.lang.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Provide API routing for patient/staff profile related requests
 */
@Singleton
@RestController
@RequestMapping(BaseController.API_CONTEXT + "/profile")
public class ProfileController implements BaseController {
    private ProfileService profileService;
    private UserEventLogService userEventLogService;

    @Autowired
    public ProfileController(ProfileService profileService, UserEventLogService userEventLogService) {
        this.profileService = profileService;
        this.userEventLogService = userEventLogService;
    }

    /**
     * Get staff profile (publicly accessible)
     */
    @RequestMapping(value = "/public/staff", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> getStaffProfile(HttpServletRequest request) {
        String username = request.getParameter(FusionConfig.URL_FIELD_USERNAME);
        if (username == null) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.GLOBAL_EXCEPTION_DEFAULT_MSG), HttpStatus.BAD_REQUEST);
        }
        try {
            StaffProfile profile = profileService.getStaffProfile(username);
            List<Language> languageList = profileService.getUserLanguageList(username);
            userEventLogService.logUserEvent(request, null, FusionConfig.EVENT_LOG_GET_STAFF_PROFILE + username);
            return new ResponseEntity<>(new StaffProfileRsp(profile, languageList), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get PCP patient of a staff
     */
    @LoginRequired
    @RequestMapping(value = "/staff/pcp", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> getStaffPcpList(@WireUser UserEntity user) {
        if (user == null) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR), HttpStatus.BAD_REQUEST);
        }
        try {
            List<BaseProfile> pcpList = profileService.getStaffPcp(user);
            return new ResponseEntity<>(new StaffPcpRsp(pcpList), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get patient profile
     */
    @LoginRequired
    @RequestMapping(value = "/patient", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> getPatientProfile(@WireUser UserEntity user) {
        if (user == null) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR), HttpStatus.BAD_REQUEST);
        }
        try {
            PatientProfile profile = profileService.getPatientProfile(user);
            Insurance insurance = profileService.getPatientInsurance(user);
            List<Language> languageList = profileService.getUserLanguageList(user);
            return new ResponseEntity<>(new PatientProfileRsp(profile, insurance, languageList), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update patient profile
     */
    @LoginRequired
    @RequestMapping(value = "/patient/update", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<BaseRsp> updatePatientProfile(HttpServletRequest request,
                                                       @RequestBody @Valid UpdatePatientProfileReq updatePatientProfileReq,
                                                       @WireUser UserEntity user, Errors errors) {
        if (user == null) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR), HttpStatus.BAD_REQUEST);
        }
        if (errors.hasErrors()) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_FORMAT_ERROR), HttpStatus.BAD_REQUEST);
        }
        try {
            profileService.updatePatientProfile(user, updatePatientProfileReq);
            userEventLogService.logUserEvent(request, user, FusionConfig.EVENT_LOG_UPDATE_PROFILE_SUCC);
            return new ResponseEntity<>(new BaseRsp(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update staff profile
     */
    @LoginRequired
    @RequestMapping(value = "/staff/update", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<BaseRsp> updateStaffProfile(HttpServletRequest request,
                                                    @RequestBody @Valid UpdateStaffProfileReq updateStaffProfileReq,
                                                    @WireUser UserEntity user, Errors errors) {
        if (user == null) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR), HttpStatus.BAD_REQUEST);
        }
        if (errors.hasErrors()) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_FORMAT_ERROR), HttpStatus.BAD_REQUEST);
        }
        try {
            profileService.updateStaffProfile(user, updateStaffProfileReq);
            userEventLogService.logUserEvent(request, user, FusionConfig.EVENT_LOG_UPDATE_PROFILE_SUCC);
            return new ResponseEntity<>(new BaseRsp(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get departments
     */
    @RequestMapping(value = "/department/all", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> getDepartmentList() {
        try {
            List<Department> result = profileService.getDepartmentList();
            return new ResponseEntity<>(new BeanListRsp<>(result), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get medical centers
     */
    @RequestMapping(value = "/medicalcenter/all", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> getMedicalCenterList() {
        try {
            List<MedicalCenter> result = profileService.getMedicalCenterList();
            return new ResponseEntity<>(new BeanListRsp<>(result), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
