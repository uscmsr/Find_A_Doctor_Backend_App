package com.oose.group6.backend.persistence.dao;

import com.oose.group6.backend.persistence.ApptStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ApptStatusDao extends CrudRepository<ApptStatusEntity, Long> {
    List<ApptStatusEntity> findAll();
    ApptStatusEntity findById(Long id);
}
