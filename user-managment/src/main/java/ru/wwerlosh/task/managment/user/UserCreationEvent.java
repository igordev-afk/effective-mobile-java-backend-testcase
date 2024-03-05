package ru.wwerlosh.task.managment.user;

import lombok.Getter;
import lombok.Setter;
import ru.wwerlosh.task.managment.user.dto.UserRegistrationDTO;

@Getter
@Setter
public class UserCreationEvent {

    private String type;

    private UserRegistrationDTO user;

}
