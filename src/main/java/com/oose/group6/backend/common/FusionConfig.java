package com.oose.group6.backend.common;

import java.util.ArrayList;
import java.util.List;

public class FusionConfig {
    public static String FIND_DOCTOR_PYTHON_PATH = "/usr/bin/python";
    public static String FIND_DOCTOR_ALGORITHM_VSM_PATH = "/home/oose/find_doc/VSM.py";

    /**
     * HTTP header field name: parsed user id
     */
    public static String HEADER_FIELD_SESSION_ID = "session_id";

    /**
     * HTTP header field name: parsed user id
     */
    public static String HEADER_FIELD_USER_ID = "HEADER_FIELD_USER_ID";

    public static final int USERNAME_MIN_LENGTH = 6;
    public static final int USERNAME_MAX_LENGTH = 50;
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 50;
    public static final int NAME_MIN_LENGTH = 1;
    public static final int NAME_MAX_LENGTH = 99;

    /**
     * Session expires after
     */
    public static final int SESSION_EXPIRE_HR = 1;

    /**
     * UA Field max length
     */
    public static final int USER_EVENT_LOG_UA_FIELD_MAX_LENGTH = 100;

    /**
     * URL field name: appointment id
     */
    public static String URL_FIELD_APPT_ID = "id";

    /**
     * URL field name: username
     */
    public static String URL_FIELD_USERNAME = "username";

    /**
     * Default error handler message
     */
    public static String GLOBAL_EXCEPTION_DEFAULT_MSG = "BAD_REQUEST";

    /**
     * User not logged in or session has expired
     */
    public static String RESPONSE_JSON_MSG_LOGIN_ERROR = "LOGIN_ERROR";

    /**
     * Session header missing in logout request
     */
    public static String RESPONSE_JSON_MSG_SESSION_HEADER_MISSING = "SESSION_HEADER_MISSING";

    /**
     * Given Appointment ID not found
     */
    public static String RESPONSE_JSON_MSG_APPT_ID_ERROR = "INVALID_APPT_ID";

    /**
     * Appointment access denied: not participant
     */
    public static String RESPONSE_JSON_MSG_APPT_NOT_PARTICIPANT = "USER_NOT_PARTICIPANT";

    /**
     * Appointment schedule denied: requester not a patient
     */
    public static String RESPONSE_JSON_MSG_USER_NOT_PATIENT_ERROR = "USER_NOT_PATIENT";

    /**
     * Appointment schedule denied: role P required, given S
     */
    public static final String RESPONSE_JSON_MSG_USER_ROLE_ERROR = "INCORRECT_USER_ROLE";

    /**
     * Error updating patient's PCP. PCP not found or found user is not a staff.
     */
    public static final String RESPONSE_JSON_MSG_PCP_ERROR = "INVALID_PCP";

    /**
     * Missing JSON field or cannot parse JSON body
     */
    public static String RESPONSE_JSON_MSG_MALFORMED_JSON = "ILLEGAL_JSON_DATA";

    /**
     * Failed request format check (NonNull / Length)
     */
    public static String RESPONSE_JSON_MSG_FORMAT_ERROR = "DATA_FORMAT_OR_LENGTH_ERROR";

    /**
     * Requested appointment time is too short, or startTime > endTime
     */
    public static String RESPONSE_JSON_MSG_TIME_TOO_SHORT = "APPT_TOO_SHORT";

    /**
     * Requested staffUsername in the appointment is not valid
     * Requested username in staff profile is not valid
     */
    public static String RESPONSE_JSON_MSG_INVALID_USERNAME = "INVALID_USERNAME";

    /**
     * Login with invalid password
     */
    public static String RESPONSE_JSON_MSG_INVALID_PASSWORD = "INVALID_PASSWORD";

    /**
     * Medical Center ID or Department ID not found
     */
    public static String RESPONSE_JSON_MSG_FIELD_ID_NOT_FOUND = "INVALID_ID";

    /**
     * Username trying to register has already been taken
     */
    public static String RESPONSE_JSON_MSG_USERNAME_ALREADY_EXISTS = "USERNAME_ALREADY_EXISTS";

    /**
     * Requested appointment has time conflict
     */
    public static String RESPONSE_JSON_MSG_TIME_CONFLICT = "TIME_CONFLICT";

    /**
     * Server is busy or DB error
     */
    public static String RESPONSE_JSON_MSG_SERVER_BUSY = "SERVER_IS_BUSY";

    /**
     * Server is busy or DB error
     */
    public static String RESPONSE_JSON_MSG_NO_RESULT = "NO_RESULT";

    /**
     * Logging
     */
    public static String EVENT_LOG_LOGIN_SUCCESS = "login";
    public static String EVENT_LOG_LOGIN_FAIL = "login fail: ";
    public static String EVENT_LOG_LOGOUT = "logout";
    public static String EVENT_LOG_REGISTER = "new user registered: ";
    public static String EVENT_LOG_CREATE_APPT = "new appt created: ";
    public static String EVENT_LOG_GET_STAFF_PROFILE = "view staff profile: ";
    public static String EVENT_LOG_CHANGE_PWD_SUCCESS = "change password";
    public static String EVENT_LOG_CHANGE_PWD_FAIL = "change password fail";
    public static String EVENT_LOG_UPDATE_PROFILE_SUCC = "update profile";

    /**
     * Minimum length of an appointment (ms)
     */
    public static final long APPT_MIN_DURATION = 10*1000;

    public static class AppointmentStatus {
        public static final int SCHEDULED = 1;
        public static final int FINISHED = 2;
        public static final int CANCELLED = 3;
        public static List<Integer> getAll() {
            List<Integer> r = new ArrayList<>();
            r.add(SCHEDULED);
            r.add(FINISHED);
            r.add(CANCELLED);
            return r;
        }
    }

    public static class UserRole {
        public static String PATIENT = "P";
        public static String STAFF = "S";
        public static List<String> getAll() {
            List<String> r = new ArrayList<>();
            r.add(PATIENT);
            r.add(STAFF);
            return r;
        }
    }

}
