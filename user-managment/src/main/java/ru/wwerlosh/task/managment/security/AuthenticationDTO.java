package ru.wwerlosh.task.managment.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthenticationDTO {

    @NotBlank(message = "email is mandatory")
    @NotNull(message = "email is mandatory")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password is mandatory")
    @NotNull(message = "password is mandatory")
    private String password;
}
