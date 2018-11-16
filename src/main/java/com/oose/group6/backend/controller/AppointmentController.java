package com.oose.group6.backend.controller;

import com.oose.group6.backend.beans.annotation.LoginRequired;
import com.oose.group6.backend.beans.annotation.WireUser;
import com.oose.group6.backend.beans.model.Appointment;
import com.oose.group6.backend.beans.model.profile.BaseProfile;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.controller.packet.request.FindDoctorReq;
import com.oose.group6.backend.controller.packet.request.ScheduleApptReq;
import com.oose.group6.backend.controller.packet.response.*;
import com.oose.group6.backend.persistence.AppointmentEntity;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.service.AppointmentService;
import com.oose.group6.backend.service.FindDoctorService;
import com.oose.group6.backend.service.UserEventLogService;
import groovy.lang.Singleton;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide appointment related API routing
 */
@Singleton
@RestController
@RequestMapping(BaseController.API_CONTEXT + "/appointment")
public class AppointmentController implements BaseController {
    public AppointmentController() {}

    private AppointmentService appointmentService;
    private UserEventLogService userEventLogService;
    private FindDoctorService findDoctorService;

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService,
                                      UserEventLogService userEventLogService,
                                      FindDoctorService findDoctorService) {
        this.appointmentService = appointmentService;
        this.userEventLogService = userEventLogService;
        this.findDoctorService = findDoctorService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    /**
     * Get appointment detail by ApptID
     */
    @LoginRequired
    @RequestMapping(value = "/detail", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> getDetail(HttpServletRequest request, @WireUser UserEntity user) {
        String apptId = request.getParameter(FusionConfig.URL_FIELD_APPT_ID);
        if (apptId == null) {
            return new ResponseEntity<>(new ErrorRsp("MISSING_ID"), HttpStatus.BAD_REQUEST);
        }
        try {
            Appointment appt = getAppointmentService().getApptDetail(user, apptId);
            ArrayList<Appointment> result = new ArrayList<>();
            result.add(appt);
            BeanListRsp<Appointment> rsp = new BeanListRsp<>(result);
            return new ResponseEntity<>(rsp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get past or future appointment
     */
    @LoginRequired
    @RequestMapping(value = {"/past", "/future"}, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> getUserAppointment(HttpServletRequest request, @WireUser UserEntity user) {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        try {
            List<Appointment> result = pattern.endsWith("past") ?
                    getAppointmentService().getPastApptByUser(user) : getAppointmentService().getFutureApptByUser(user);
            BeanListRsp<Appointment> rsp = new BeanListRsp<>(result);
            return new ResponseEntity<>(rsp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create new appointment
     */
    @LoginRequired
    @RequestMapping(value = "/schedule", method = RequestMethod.PUT,
            consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<BaseRsp> scheduleNew(HttpServletRequest servletRequest,
                                               @WireUser UserEntity user,
                                               @RequestBody @Valid ScheduleApptReq scheduleApptReq,
                                               Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_MALFORMED_JSON),
                    HttpStatus.BAD_REQUEST);
        }
        Appointment appt = new Appointment();
        try {
            BeanUtils.copyProperties(scheduleApptReq, appt);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_MALFORMED_JSON),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            AppointmentEntity newAppt = getAppointmentService().scheduleAppt(user, appt);
            userEventLogService.logUserEvent(servletRequest, user,
                    FusionConfig.EVENT_LOG_CREATE_APPT + newAppt.getId());
            return new ResponseEntity<>(new ScheduleApptRsp(newAppt.getId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Find a doctor
     */
    @RequestMapping(value = "/finddoctor", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<BaseRsp> findDoctor(@RequestBody FindDoctorReq findDoctorReq) {
        if (findDoctorReq.getSymptom() == null || findDoctorReq.getSymptom().isEmpty()) {
            return new ResponseEntity<>(new ErrorRsp(FusionConfig.RESPONSE_JSON_MSG_MALFORMED_JSON),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            List<BaseProfile> dList = findDoctorService.getDoctorList(findDoctorReq.getSymptom());
            return new ResponseEntity<>(new FindDoctorRsp(dList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
