package com.oose.group6.backend.persistence.dao;

import com.oose.group6.backend.persistence.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface DepartmentDao extends CrudRepository<DepartmentEntity, Long> {
    List<DepartmentEntity> findAll();
    DepartmentEntity findById(Long id);
}
