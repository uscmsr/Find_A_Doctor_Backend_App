package com.oose.group6.backend.persistence.dao;

import com.oose.group6.backend.persistence.MedicalCenterEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MedicalCenterDao extends CrudRepository<MedicalCenterEntity, Long> {
    List<MedicalCenterEntity> findAll();
    MedicalCenterEntity findById(Long id);
}
