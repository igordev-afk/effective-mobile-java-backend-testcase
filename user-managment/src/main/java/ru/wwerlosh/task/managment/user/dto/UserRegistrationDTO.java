package ru.wwerlosh.task.managment.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    private Long id;

    @NotBlank(message = "login is mandatory")
    @NotNull(message = "login is mandatory")
    private String login;

    @NotBlank(message = "password is mandatory")
    @NotNull(message = "password is mandatory")
    private String password;

    @NotBlank(message = "phone is mandatory")
    @NotNull(message = "phone is mandatory")
    @Pattern(regexp = "\\+\\d{11}", message = "invalid phone number")
    private String phone;

    @NotBlank(message = "email is mandatory")
    @NotNull(message = "email is mandatory")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "firstName is mandatory")
    @NotNull(message = "firstName is mandatory")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    @NotNull(message = "lastName is mandatory")
    private String lastName;

    private String patronymic;

    @NotNull(message = "dateOfBirth is mandatory")
    @PastOrPresent(message = "must contain a past date or today's date")
    private LocalDate dateOfBirth;

    @NotNull(message = "amount is mandatory")
    @PositiveOrZero(message = "must be greater than or equal to 0")
    private BigDecimal amount;
}
