package com.oose.group6.backend.persistence.dao;

import com.oose.group6.backend.persistence.InsuranceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface InsuranceDao extends CrudRepository<InsuranceEntity, Long> {
    List<InsuranceEntity> findAll();
}
