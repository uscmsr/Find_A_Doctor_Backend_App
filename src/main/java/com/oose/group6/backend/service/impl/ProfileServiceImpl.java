package com.oose.group6.backend.service.impl;

import com.oose.group6.backend.beans.model.Department;
import com.oose.group6.backend.beans.model.Insurance;
import com.oose.group6.backend.beans.model.Language;
import com.oose.group6.backend.beans.model.MedicalCenter;
import com.oose.group6.backend.beans.model.profile.BaseProfile;
import com.oose.group6.backend.beans.model.profile.ICommonProfile;
import com.oose.group6.backend.beans.model.profile.PatientProfile;
import com.oose.group6.backend.beans.model.profile.StaffProfile;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.controller.packet.request.UpdatePatientProfileReq;
import com.oose.group6.backend.controller.packet.request.UpdateStaffProfileReq;
import com.oose.group6.backend.persistence.*;
import com.oose.group6.backend.persistence.dao.*;
import com.oose.group6.backend.service.ProfileService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide user profile related service
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    private UserDao userDao;
    private MedicalCenterDao medicalCenterDao;
    private DepartmentDao departmentDao;
    private SessionFactory sessionFactory;

    @Autowired
    public ProfileServiceImpl(UserDao userDao, MedicalCenterDao medicalCenterDao, DepartmentDao departmentDao,
                          SessionFactory sessionFactory) {
        this.userDao = userDao;
        this.departmentDao = departmentDao;
        this.medicalCenterDao = medicalCenterDao;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Generate Patient Profile
     */
    @Override
    public PatientProfile getPatientProfile(UserEntity user) throws ProfileServiceException {
        PatientEntity patientEntity = checkRoleIsPatient(user);
        // basic info
        PatientProfile profile = new PatientProfile();
        BeanUtils.copyProperties(patientEntity, profile);
        profile.setUsername(user.getUsername());
        // pcp info
        if (patientEntity.getMedicalStaff() != null) {
            profile.setPcpUsername(patientEntity.getMedicalStaff().getUser().getUsername());
            profile.setPcpName(patientEntity.getMedicalStaff().getName());
        }
        return profile;
    }

    /**
     * Generate Staff Profile
     */
    @Override
    public StaffProfile getStaffProfile(String username) throws ProfileServiceException {
        MedicalStaffEntity staffEntity = checkRoleIsStaff(findUserByUsername(username));
        // basic info
        StaffProfile profile = new StaffProfile();
        BeanUtils.copyProperties(staffEntity, profile);
        // cross entity info
        profile.setUsername(username);
        Department department = new Department();
        BeanUtils.copyProperties(staffEntity.getDepartment(), department);
        profile.setDepartment(department);
        MedicalCenter center = new MedicalCenter();
        BeanUtils.copyProperties(staffEntity.getMedicalCenter(), center);
        profile.setMedicalCenter(center);
        for (StaffWorkdayMappingEntity m : staffEntity.getStaffWorkday()) {
            profile.getWorkdays().add(new StaffProfile.Workday(m.getWorkday().getId(), m.getWorkday().getDay()));
        }
        return profile;
    }

    @Override
    public void updatePatientProfile(UserEntity user, UpdatePatientProfileReq updatePatientProfileReq)
            throws ProfileServiceException {
        // convert to bean
        PatientProfile newProfile = new PatientProfile();
        BeanUtils.copyProperties(updatePatientProfileReq, newProfile);

        PatientEntity patientEntity = checkRoleIsPatient(user);
        validateProfileNameAndGender(newProfile);
        // validate pcp change
        MedicalStaffEntity newPcpEntity = validatePcpUpdate(newProfile.getPcpUsername());
        // update
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.evict(patientEntity);
            // update patient profile
            BeanUtils.copyProperties(newProfile, patientEntity, "id");
            // update pcp
            patientEntity.setMedicalStaff(newPcpEntity);
            session.merge(patientEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_SERVER_BUSY);
        } finally {
            session.close();
        }
    }

    private MedicalStaffEntity validatePcpUpdate(String username) throws ProfileServiceException {
        if (username == null) { return null; }
        UserEntity user = userDao.findByUsername(username);
        if (user == null) { throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_PCP_ERROR); }
        return checkRoleIsStaff(user);
    }

    private void validateProfileNameAndGender(ICommonProfile newProfile) throws ProfileServiceException {
        // check name
        newProfile.setName(newProfile.getName().trim());
        if (newProfile.getName().isEmpty()) {
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_FORMAT_ERROR);
        }
        // check gender
        if (!newProfile.getGender().matches("M") && !newProfile.getGender().matches("F")) {
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_FORMAT_ERROR);
        }
    }

    @Override
    public void updateStaffProfile(UserEntity user, UpdateStaffProfileReq updateStaffProfileReq)
            throws ProfileServiceException {
        // convert to bean
        StaffProfile newProfile = new StaffProfile();
        BeanUtils.copyProperties(updateStaffProfileReq, newProfile);

        MedicalStaffEntity staffEntity = checkRoleIsStaff(user);
        validateProfileNameAndGender(newProfile);
        // valid department and medical center Id
        MedicalCenterEntity medicalCenterEntity = medicalCenterDao.findById(updateStaffProfileReq.getMedicalCenterId());
        DepartmentEntity departmentEntity = departmentDao.findById(updateStaffProfileReq.getDepartmentId());
        if (medicalCenterEntity == null || departmentEntity == null) {
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_FIELD_ID_NOT_FOUND);
        }
        // update
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.evict(staffEntity);
            // update patient profile
            BeanUtils.copyProperties(newProfile, staffEntity, "id", "workdays");
            staffEntity.setDepartment(departmentEntity);
            staffEntity.setMedicalCenter(medicalCenterEntity);
            session.merge(staffEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_SERVER_BUSY);
        } finally {
            session.close();
        }
    }

    /**
     * Generate PCP Patient List for a staff
     */
    @Override
    public List<BaseProfile> getStaffPcp(UserEntity userEntity) throws ProfileServiceException {
        MedicalStaffEntity staffEntity = checkRoleIsStaff(userEntity);
        List<BaseProfile> pcpList = new ArrayList<>();
        for (PatientEntity p : staffEntity.getPatients()) {
            pcpList.add(new BaseProfile(p.getUser().getUsername(), p.getName()));
        }
        return pcpList;
    }

    @Override
    public Insurance getPatientInsurance(UserEntity user) throws ProfileServiceException {
        PatientEntity patientEntity = checkRoleIsPatient(user);
        Insurance insurance = new Insurance();
        BeanUtils.copyProperties(patientEntity.getInsurance(), insurance);
        return insurance;
    }

    @Override
    public List<Language> getUserLanguageList(UserEntity user) throws ProfileServiceException {
        List<Language> result = new ArrayList<>();
        for (UserLanguageMappingEntity mappingEntity : user.getUserLanguageMappings()) {
            Language l = new Language();
            BeanUtils.copyProperties(mappingEntity.getLanguage(), l);
            result.add(l);
        }
        return result;
    }

    @Override
    public List<Language> getUserLanguageList(String username) throws ProfileServiceException {
        return getUserLanguageList(findUserByUsername(username));
    }

    @Override
    public List<MedicalCenter> getMedicalCenterList() throws ProfileServiceException {
        List<MedicalCenter> result = new ArrayList<>();
        for (MedicalCenterEntity entity : medicalCenterDao.findAll()) {
            MedicalCenter m = new MedicalCenter();
            BeanUtils.copyProperties(entity, m);
            result.add(m);
        }
        return result;
    }

    @Override
    public List<Department> getDepartmentList() throws ProfileServiceException {
        List<Department> result = new ArrayList<>();
        for (DepartmentEntity entity : departmentDao.findAll()) {
            Department m = new Department();
            BeanUtils.copyProperties(entity, m);
            result.add(m);
        }
        return result;
    }

    private PatientEntity checkRoleIsPatient(UserEntity user) throws ProfileServiceException {
        if (user.getPatient() == null) {
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_USER_ROLE_ERROR);
        }
        return user.getPatient();
    }

    private MedicalStaffEntity checkRoleIsStaff(UserEntity user) throws ProfileServiceException {
        if (user.getMedicalStaff() == null) {
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_USER_ROLE_ERROR);
        }
        return user.getMedicalStaff();
    }

    private UserEntity findUserByUsername(String username) throws ProfileServiceException {
        UserEntity userEntity = userDao.findByUsername(username);
        if (userEntity == null) {
            throw new ProfileServiceException(FusionConfig.RESPONSE_JSON_MSG_INVALID_USERNAME);
        }
        return userEntity;
    }

}
