package com.project3.dto;

import com.project3.models.RoleEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private MultipartFile avatar;

    private String urlAvatar;

    @NotEmpty(message = "Name should not be empty.")
    private String name;

    @NotEmpty(message = "Username should not be empty.")
    private String username;

    @NotEmpty(message = "Email should not be empty.")
    private String email;

    private String password;

    @NotEmpty(message = "Address should not be empty.")
    private String address;

    @NotNull(message = "Birthday should not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private RoleEntity role;
}
