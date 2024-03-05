package ru.wwerlosh.task.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationEvent {

    private String type;

    private UserRegistrationDTO user;

}
