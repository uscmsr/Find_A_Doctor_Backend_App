package com.oose.group6.backend.common;

import com.oose.group6.backend.controller.packet.response.BaseRsp;
import com.oose.group6.backend.controller.packet.response.ErrorRsp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * Controller cannot parse malformed JSON input
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<BaseRsp> unreadableReqExceptionHandler(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorRsp(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseRsp> defaultExceptionHandler(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorRsp(FusionConfig.GLOBAL_EXCEPTION_DEFAULT_MSG), HttpStatus.BAD_REQUEST);
    }
}

