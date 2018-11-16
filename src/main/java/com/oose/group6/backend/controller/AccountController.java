package com.oose.group6.backend.controller;

import com.oose.group6.backend.beans.annotation.LoginRequired;
import com.oose.group6.backend.beans.annotation.WireUser;
import com.oose.group6.backend.beans.manager.SessionManager;
import com.oose.group6.backend.beans.model.HttpSession;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.controller.packet.request.ChangePasswordReq;
import com.oose.group6.backend.controller.packet.request.LoginReq;
import com.oose.group6.backend.controller.packet.request.RegisterReq;
import com.oose.group6.backend.controller.packet.response.BaseRsp;
import com.oose.group6.backend.controller.packet.response.ErrorRsp;
import com.oose.group6.backend.controller.packet.response.LoginRsp;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.service.AccountService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Provide API routing for account related requests
 */
@Singleton
@RestController
@RequestMapping(BaseController.API_CONTEXT + "/user")
public class AccountController implements BaseController {
    private AccountService accountService;
    private UserEventLogService userEventLogService;
    private SessionManager sessionManager;

    @Autowired
    public AccountController(SessionManager sessionManager,
                             UserEventLogService userEventLogService, AccountService accountService) {
        this.sessionManager = sessionManager;
        this.accountService = accountService;
        this.userEventLogService = userEventLogService;
    }

    /**
     * User login
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<BaseRsp> userLogin(HttpServletRequest request, @RequestBody LoginReq loginReq) {
        String username = loginReq.getUsername();
        String password = loginReq.getPassword();
        try {
            UserEntity user = accountService.verifyLogin(username, password);
            HttpSession session = sessionManager.createSession(user.getId());
            LoginRsp rsp = new LoginRsp(session.getSessionId(), user.getRole(), accountService.recordLoginTime(user));
            userEventLogService.logUserEvent(request, user, FusionConfig.EVENT_LOG_LOGIN_SUCCESS);
            return new ResponseEntity<>(rsp, HttpStatus.OK);
        } catch (Exception e) {
            userEventLogService.logUserEvent(request, null, FusionConfig.EVENT_LOG_LOGIN_FAIL + username);
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Change password
     */
    @LoginRequired
    @RequestMapping(value = "/changemm", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<BaseRsp> changePassword(HttpServletRequest request,
                                                  @RequestBody @Valid ChangePasswordReq changePasswordReq,
                                                  @WireUser UserEntity user, Errors errors) {
        try {
            if (errors.hasErrors()) {
                throw new IllegalArgumentException(FusionConfig.RESPONSE_JSON_MSG_FORMAT_ERROR);
            }
            accountService.changePassword(user, changePasswordReq.getOldPassword(), changePasswordReq.getNewPassword());
            BaseRsp rsp = new BaseRsp();
            userEventLogService.logUserEvent(request, user, FusionConfig.EVENT_LOG_CHANGE_PWD_SUCCESS);
            return new ResponseEntity<>(rsp, HttpStatus.OK);
        } catch (Exception e) {
            userEventLogService.logUserEvent(request, user, FusionConfig.EVENT_LOG_CHANGE_PWD_FAIL);
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * User logout
     */
    @LoginRequired
    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BaseRsp> userLogout(HttpServletRequest request, @WireUser UserEntity user) {
        try {
            String id = request.getHeader(FusionConfig.HEADER_FIELD_SESSION_ID);
            accountService.logoutSession(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        userEventLogService.logUserEvent(request, user, FusionConfig.EVENT_LOG_LOGOUT);
        return new ResponseEntity<>(new BaseRsp(), HttpStatus.OK);
    }

    /**
     * Registration
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<BaseRsp> userRegistration(HttpServletRequest httpServletRequest,
                                                    @RequestBody @Valid RegisterReq req, Errors errors) {
        try {
            if (errors.hasErrors()) {
                throw new IllegalArgumentException(FusionConfig.RESPONSE_JSON_MSG_FORMAT_ERROR);
            }
            accountService.createNewUser(req.getUsername(), req.getPassword(), req.getUserType(), req.getName());
            userEventLogService.logUserEvent(httpServletRequest, null,
                    FusionConfig.EVENT_LOG_REGISTER + req.getUsername());
            LoginReq loginReq = new LoginReq();
            BeanUtils.copyProperties(req, loginReq);
            return userLogin(httpServletRequest, loginReq);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
