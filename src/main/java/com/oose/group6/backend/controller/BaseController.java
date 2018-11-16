package com.oose.group6.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*", maxAge = 3600)
public interface BaseController {
     String API_CONTEXT = "/api/v1";

}
