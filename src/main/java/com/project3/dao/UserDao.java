package com.project3.dao;

import com.project3.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<UserEntity, Long> {
    @Query("select count(u) from UserEntity u")
    int countUser();

    @Query("select u from com.project3.models.UserEntity u where username=:username")
    UserEntity findByUsername(String username);

    @Query("select u from UserEntity u where email=:email")
    UserEntity findByEmail(String email);

    @Query("select u from UserEntity u where username=:input or email=:input")
    UserEntity findByUsernameOrEmail(String input);

    @Query("select count(u) from UserEntity u where username=:username")
    int countUserName(String username);

    @Query("select count(u) from UserEntity u where email=:email")
    int countEmail(String email);

    @Query("select u from UserEntity u where username!='admin'")
    List<UserEntity> findAll();

    @Query("select u from UserEntity u where email ilike :search order by id asc")
    List<UserEntity> findUsersByEmail(String search);
}
