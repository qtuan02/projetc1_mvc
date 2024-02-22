package com.project3.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterDto {
    
    @NotEmpty(message = "Username should not be empty.")
    private String username;

    @NotEmpty(message = "Email should not be empty.")
    private String email;

    @NotEmpty(message = "Password should not be empty.")
    private String password;

    @NotEmpty(message = "Repeat password should not be empty.")
    private String repeat;
}
