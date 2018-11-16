package com.oose.group6.backend.persistence.dao;

import com.oose.group6.backend.persistence.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserDao extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    UserEntity findById(Long id);
    UserEntity findByUsername(String username);
}
