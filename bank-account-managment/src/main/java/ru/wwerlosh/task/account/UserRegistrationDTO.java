package ru.wwerlosh.task.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    private Long id;

    private String login;

    private String password;

    private String phone;

    private String email;

    private String firstName;

    private String lastName;

    private String patronymic;

    private LocalDate dateOfBirth;

    private BigDecimal amount;
}
