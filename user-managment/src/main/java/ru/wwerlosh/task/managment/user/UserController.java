package ru.wwerlosh.task.managment.user;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.task.managment.user.dto.UserRegistrationDTO;
import ru.wwerlosh.task.managment.user.dto.UserUpdateDTO;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.createUser(userRegistrationDTO);
        return ResponseEntity.ok("User creation is processing");
    }


    @PatchMapping("/users/{id}/update")
    public User updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long id) {
        return userService.updateUser(userUpdateDTO, id);
    }



    @PatchMapping("/users/{id}/delete_field")
    public User deleteUserField(@PathVariable Long id,
                                @RequestParam @Pattern(regexp = "email|phone", message = "Invalid deletedType") String type) {
        return userService.deleteUserFieldByType(id, type);
    }

}
