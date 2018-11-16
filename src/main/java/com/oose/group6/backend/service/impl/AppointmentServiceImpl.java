package com.oose.group6.backend.service.impl;


import com.oose.group6.backend.beans.model.Appointment;
import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.persistence.AppointmentEntity;
import com.oose.group6.backend.persistence.MedicalStaffEntity;
import com.oose.group6.backend.persistence.UserEntity;
import com.oose.group6.backend.persistence.dao.AppointmentDao;
import com.oose.group6.backend.persistence.dao.ApptStatusDao;
import com.oose.group6.backend.persistence.dao.UserDao;
import com.oose.group6.backend.service.AppointmentService;
import org.hibernate.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide login and registration service
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private UserDao userDao;
    private AppointmentDao appointmentDao;
    private ApptStatusDao apptStatusDao;
    private SessionFactory sessionFactory;

    private interface ApptListFetchStrategy {
        List<AppointmentEntity> fetchApptListForPatient();
        List<AppointmentEntity> fetchApptListForStaff();
    }

    @Autowired
    public AppointmentServiceImpl(UserDao userDao, AppointmentDao appointmentDao, ApptStatusDao apptStatusDao,
                              SessionFactory sessionFactory) {
        this.userDao = userDao;
        this.appointmentDao = appointmentDao;
        this.apptStatusDao = apptStatusDao;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Get Appointment Detail
     */
    @Override
    public Appointment getApptDetail(UserEntity user, String apptId) throws AppointmentServiceException {
        if (user == null) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR);
        }
        AppointmentEntity apptEntity = appointmentDao.findById(Long.parseLong(apptId));
        if (null == apptEntity) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_APPT_ID_ERROR);
        }
        // check participation
        long patientStaffId = user.getPatient()==null ? user.getMedicalStaff().getId() : user.getPatient().getId();
        if (patientStaffId != apptEntity.getPatient().getId() &&
                patientStaffId != apptEntity.getMedicalStaff().getId()) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_APPT_NOT_PARTICIPANT);
        }
        return convertApptEntityToBean(apptEntity);
    }

    /**
     * Schedule new appointment
     */
    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AppointmentEntity scheduleAppt(UserEntity patientUser, Appointment appointment)
            throws AppointmentServiceException {
        // check null
        if (patientUser == null) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR);
        }
        // check requester cannot be a staff
        if (! patientUser.getRole().equals(FusionConfig.UserRole.PATIENT)) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_USER_NOT_PATIENT_ERROR);
        }
        // check staff exists
        UserEntity staffEntity = userDao.findByUsername(appointment.getStaffUsername());
        if (null == staffEntity) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_INVALID_USERNAME);
        }
        MedicalStaffEntity staff = staffEntity.getMedicalStaff();
        if (null == staff) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_INVALID_USERNAME);
        }
        // check format and duration of startTime and endTime
        if (appointment.getEndTime().getTime() - appointment.getStartTime().getTime() < FusionConfig.APPT_MIN_DURATION
                || appointment.getStartTime().getTime() % 1000 != 0
                || appointment.getEndTime().getTime() % 1000 != 0) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_TIME_TOO_SHORT);
        }
        // check time conflict
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "FROM AppointmentEntity A " +
                    "WHERE ((:st > A.startTime AND :st < A.endTime) " +
                    "OR (:et > A.startTime AND :et < A.endTime) " +
                    "OR (:st = A.startTime) OR (:et = A.endTime)) " +
                    "AND (A.medicalStaff.id = :staffid OR A.patient.id = :patientid)";
            Query query = session.createQuery(hql);
            query.setParameter("st",appointment.getStartTime());
            query.setParameter("et",appointment.getEndTime());
            query.setParameter("staffid", staff.getId());
            query.setParameter("patientid", patientUser.getPatient().getId());
            List hResult = query.list();
            if (! hResult.isEmpty()) {
                throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_TIME_CONFLICT);
            }
            // generate new persistent object
            AppointmentEntity appointmentEntity = new AppointmentEntity();
            BeanUtils.copyProperties(appointment, appointmentEntity, "id");
            appointmentEntity.setMedicalStaff(staff);
            appointmentEntity.setPatient(patientUser.getPatient());
            appointmentEntity.setApptStatus(apptStatusDao.findById((long)FusionConfig.AppointmentStatus.SCHEDULED));
            // commit
            session.save(appointmentEntity);
            transaction.commit();
            // return result
            return appointmentEntity;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_SERVER_BUSY);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Appointment> getPastApptByUser(UserEntity user) throws AppointmentServiceException {
        return getAppointmentListByUser(user, new ApptListFetchStrategy() {
            @Override
            public List<AppointmentEntity> fetchApptListForPatient() {
                return appointmentDao.findAllByPatientAndEndTimeIsBefore(
                        user.getPatient(), new Timestamp(System.currentTimeMillis()));
            }

            @Override
            public List<AppointmentEntity> fetchApptListForStaff() {
                return appointmentDao.findAllByMedicalStaffAndEndTimeIsBefore(
                        user.getMedicalStaff(), new Timestamp(System.currentTimeMillis()));
            }
        });
    }

    @Override
    public List<Appointment> getFutureApptByUser(UserEntity user) throws AppointmentServiceException {
        return getAppointmentListByUser(user, new ApptListFetchStrategy() {
            @Override
            public List<AppointmentEntity> fetchApptListForPatient() {
                return appointmentDao.findAllByPatientAndEndTimeIsAfter(
                        user.getPatient(), new Timestamp(System.currentTimeMillis()));
            }

            @Override
            public List<AppointmentEntity> fetchApptListForStaff() {
                return appointmentDao.findAllByMedicalStaffAndEndTimeIsAfter(
                        user.getMedicalStaff(), new Timestamp(System.currentTimeMillis()));
            }
        });
    }

    private List<Appointment> getAppointmentListByUser(UserEntity user, ApptListFetchStrategy strategy)
            throws AppointmentServiceException {
        List<AppointmentEntity> apptEntities = null;

        if (user == null) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_LOGIN_ERROR);
        } else if (user.getRole().equals(FusionConfig.UserRole.PATIENT)) {
            apptEntities = strategy.fetchApptListForPatient();
        } else if (user.getRole().equals(FusionConfig.UserRole.STAFF)) {
            apptEntities = strategy.fetchApptListForStaff();
        }
        if (null == apptEntities) {
            throw new AppointmentServiceException(FusionConfig.RESPONSE_JSON_MSG_NO_RESULT);
        }
        return convertApptEntityListToBean(apptEntities);
    }

    private static List<Appointment> convertApptEntityListToBean(List<AppointmentEntity> apptEntities) {
        List<Appointment> result = new ArrayList<>(apptEntities.size());
        for (AppointmentEntity appointmentEntity : apptEntities) {
            result.add(convertApptEntityToBean(appointmentEntity));
        }
        return result;
    }

    private static Appointment convertApptEntityToBean(AppointmentEntity apptEntity) {
        Appointment appt = new Appointment();
        BeanUtils.copyProperties(apptEntity, appt);
        appt.setStaffName(apptEntity.getMedicalStaff().getName());
        appt.setPatientName(apptEntity.getPatient().getName());
        appt.setStaffUsername(apptEntity.getMedicalStaff().getUser().getUsername());
        appt.setPatientUsername(apptEntity.getPatient().getUser().getUsername());
        return appt;
    }

}
