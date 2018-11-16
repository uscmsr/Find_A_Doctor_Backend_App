package com.oose.group6.backend.service.impl;


import com.oose.group6.backend.beans.model.profile.BaseProfile;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.persistence.dao.UserDao;
import com.oose.group6.backend.service.FindDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide user profile related service
 */
@Service
public class FindDoctorServiceImpl implements FindDoctorService {

    private UserDao userDao;

    @Autowired
    public FindDoctorServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override public List<BaseProfile> getDoctorList(String input) throws FindDoctorServiceException {
        String pythonPath = FusionConfig.FIND_DOCTOR_PYTHON_PATH;
        String algPath = FusionConfig.FIND_DOCTOR_ALGORITHM_VSM_PATH;
        List<BaseProfile> staffList = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(pythonPath + " " + algPath + " " + input);
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            while ((s = output.readLine()) != null) {
                if (s.matches("\\d+")) {
                    UserEntity staffUser = userDao.findById(Long.parseLong(s));
                    staffList.add(new BaseProfile(staffUser.getUsername(), staffUser.getMedicalStaff().getName()));
                }
            }
        } catch (Exception e) {
            throw new FindDoctorServiceException(FusionConfig.RESPONSE_JSON_MSG_NO_RESULT);
        }
        return staffList;
    }

}
