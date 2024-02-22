package com.project3.dao;

import com.project3.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<RoleEntity, Long> {
    @Query("select r from RoleEntity r where name=:name")
    RoleEntity findByName(String name);
}
