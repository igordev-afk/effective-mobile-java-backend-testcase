package ru.wwerlosh.task.managment.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.wwerlosh.task.managment.user.dto.UserRegistrationDTO;
import ru.wwerlosh.task.managment.user.dto.UserUpdateDTO;
import ru.wwerlosh.task.managment.validation.UserNotFoundException;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, UserCreationEvent> kafkaTemplate;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       KafkaTemplate<String, UserCreationEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createUser(UserRegistrationDTO userRegistrationDTO) {
        log.debug("Attempting to create user: {}", userRegistrationDTO);

        if (userRepository.existsByLogin(userRegistrationDTO.getLogin())) {
            throw new IllegalArgumentException("Login occupied");
        }

        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email occupied");
        }

        if (userRepository.existsByPhone(userRegistrationDTO.getPhone())) {
            throw new IllegalArgumentException("Phone occupied");
        }

        User user = new User();
        try {

            user.setLogin(userRegistrationDTO.getLogin());
            user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
            user.setPhone(userRegistrationDTO.getPhone());
            user.setEmail(userRegistrationDTO.getEmail());
            user.setFirstName(userRegistrationDTO.getFirstName());
            user.setLastName(userRegistrationDTO.getLastName());
            user.setPatronymic(userRegistrationDTO.getPatronymic());
            user.setDateOfBirth(userRegistrationDTO.getDateOfBirth());

            user = userRepository.save(user);

            userRegistrationDTO.setId(user.getId());

            UserCreationEvent event = new UserCreationEvent();
            event.setUser(userRegistrationDTO);
            event.setType("USER_CREATED");
            kafkaTemplate.send("new-users", event);

            log.info("User created successfully: {}", userRegistrationDTO);
        } catch (Exception e) {
            log.error("Error occurred with registration user: {}", e.getMessage());
            throw new IllegalStateException("Error occurred with registration user");
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User updateUser(UserUpdateDTO userUpdateDTO, Long id) {
        log.debug("Attempting to update user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        String email = userUpdateDTO.getEmail();
        String phone = userUpdateDTO.getPhone();

        if (email != null && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email occupied");
        }

        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Phone occupied");
        }

        if (email != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }

        if (phone != null) {
            user.setPhone(userUpdateDTO.getPhone());
        }

        log.info("User updated successfully with ID: {}", id);
        return userRepository.save(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User deleteUserFieldByType(Long id, String type) {
        log.debug("Attempting to delete field '{}' for user with ID: {}", type, id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        String userDeletedType = null;
        if ("email".equals(type)) {
            userDeletedType = user.getEmail();
        } else {
            userDeletedType = user.getPhone();
        }

        if (userDeletedType == null) {
            throw new IllegalStateException("The specified field is already null");
        }

        if (user.getEmail() == null && user.getPhone() != null || user.getEmail() != null && user.getPhone() == null) {
            throw new IllegalStateException("Unable to delete the last remaining contact information");
        }

        if ("email".equals(type)) {
            user.setEmail(null);
        } else {
            user.setPhone(null);
        }

        log.debug("Successfully deleted field '{}' for user with ID: {}", type, id);
        return userRepository.save(user);
    }

}
