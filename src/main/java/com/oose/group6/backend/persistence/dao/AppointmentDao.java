package com.oose.group6.backend.persistence.dao;

import com.oose.group6.backend.persistence.AppointmentEntity;
import com.oose.group6.backend.persistence.MedicalStaffEntity;
import com.oose.group6.backend.persistence.PatientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface AppointmentDao extends CrudRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findAll();
    AppointmentEntity findById(Long id);
    List<AppointmentEntity> findAllByPatientAndEndTimeIsBefore(PatientEntity patientEntity, Timestamp endTime);
    List<AppointmentEntity> findAllByPatientAndEndTimeIsAfter(PatientEntity patientEntity, Timestamp endTime);
    List<AppointmentEntity> findAllByMedicalStaffAndEndTimeIsBefore(MedicalStaffEntity medicalStaffEntity, Timestamp endTime);
    List<AppointmentEntity> findAllByMedicalStaffAndEndTimeIsAfter(MedicalStaffEntity medicalStaffEntity, Timestamp endTime);

}
