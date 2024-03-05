package ru.wwerlosh.task.managment.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.wwerlosh.task.managment.validation.AtLeastOneNotNull;

@Data
@AtLeastOneNotNull
public class UserUpdateDTO {
    @Pattern(regexp = "\\+\\d{11}", message = "invalid phone number")
    private String phone;

    @Email(message = "invalid email")
    private String email;
}
