package com.project3.service;

import com.project3.dto.RegisterDto;
import com.project3.dto.UserDto;
import com.project3.models.RoleEntity;
import com.project3.models.UserEntity;

import java.util.List;

public interface IUserService {
    int countUser();
    void save(RegisterDto userDto);
    int countUsername(String username);
    int countEmail(String email);
    UserDto findByUsername(String username);
    List<UserDto> findAll();
    void deleteUserById(Long id);
    UserDto findUserById(Long id);
    List<RoleEntity> findAllRoles();
    void editUser(UserDto userDto);
    List<UserDto> findUserByEmail(String search);
}
