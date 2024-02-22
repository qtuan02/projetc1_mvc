package com.project3.service.impl;

import com.project3.dao.RoleDao;
import com.project3.dao.UserDao;
import com.project3.dto.ProductDto;
import com.project3.dto.RegisterDto;
import com.project3.dto.UserDto;
import com.project3.mapper.ProductMapper;
import com.project3.mapper.UserMapper;
import com.project3.models.RoleEntity;
import com.project3.models.UserEntity;
import com.project3.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserService implements IUserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int countUser() {
        return userDao.countUser();
    }

    @Override
    public void save(RegisterDto userDto) {
        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(roleDao.findByName("USER"));
        userDao.save(user);
    }

    @Override
    public int countUsername(String username) {
        return userDao.countUserName(username);
    }

    @Override
    public int countEmail(String email) {
        return userDao.countEmail(email);
    }

    @Override
    public UserDto findByUsername(String username) {
        return UserMapper.mapToUserDto(userDao.findByUsername(username));
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll().stream().map(user -> UserMapper.mapToUserDto(user)).toList();
    }

    @Override
    public void deleteUserById(Long id) {
        UserEntity userEntity = userDao.findById(id).get();
        if(userEntity.getAvatar() != null){
            String filePath = "public/uploads/avatars/"+userEntity.getAvatar();
            File imageFile = new File(filePath);
            if(imageFile.exists()){
                imageFile.delete();
            }
        }
        userDao.deleteById(id);
    }

    @Override
    public UserDto findUserById(Long id) {
        return UserMapper.mapToUserDto(userDao.findById(id).get());
    }

    @Override
    public List<RoleEntity> findAllRoles() {
        return roleDao.findAll();
    }

    @Override
    public void editUser(UserDto userDto) {
        DateTimeFormatter typeDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp date = Timestamp.valueOf(LocalDateTime.now().format(typeDate));

        DateTimeFormatter typeTimeUrlAvatar = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeUrlAvatar = LocalDateTime.now().format(typeTimeUrlAvatar);

        UserDto user = findUserById(userDto.getId());
        userDto.setPassword(user.getPassword());


        if(userDto.getAvatar().isEmpty()){
            userDao.save(UserMapper.mapToUser(userDto));
        }else{
            String filePath = "public/uploads/avatars/";
            File deleteAvatarFile = new File(filePath+userDto.getUrlAvatar());
            if(deleteAvatarFile.exists()){
                deleteAvatarFile.delete();
            }

            userDto.setUrlAvatar(timeUrlAvatar+userDto.getAvatar().getOriginalFilename());
            try {
                Path path = Paths.get(filePath + userDto.getUrlAvatar());
                Files.copy(userDto.getAvatar().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                e.printStackTrace();
            }

            userDao.save(UserMapper.mapToUser(userDto));
        }
    }

    @Override
    public List<UserDto> findUserByEmail(String search) {
        return userDao.findUsersByEmail("%"+search+"%").stream().map(user -> UserMapper.mapToUserDto(user)).toList();
    }


}
