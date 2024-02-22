package com.project3.mapper;

import com.project3.dto.UserDto;
import com.project3.models.UserEntity;

public class UserMapper {
    public static UserDto mapToUserDto(UserEntity userEntity){
        UserDto userDto = UserDto.builder()
                .id(userEntity.getId())
                .urlAvatar(userEntity.getAvatar())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .address(userEntity.getAddress())
                .birthday(userEntity.getBirthday())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
        return userDto;
    }

    public static UserEntity mapToUser(UserDto userDto){
        UserEntity user = UserEntity.builder()
                .id(userDto.getId())
                .avatar(userDto.getUrlAvatar())
                .name(userDto.getName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .address(userDto.getAddress())
                .birthday(userDto.getBirthday())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .build();
        return user;
    }
}
