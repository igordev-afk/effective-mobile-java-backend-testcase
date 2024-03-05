package ru.wwerlosh.task.managment.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCreationRollbackListener {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserCreationRollbackListener(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "rollback-user-creation", groupId = "users-group")
    public void listen(String event) {

        try {
            UserCreationEvent userEvent = objectMapper.readValue(event, UserCreationEvent.class);

            Optional<User> user = userRepository.findById(userEvent.getUser().getId());

            user.ifPresentOrElse(
                    userToDelete -> {
                        userRepository.delete(userToDelete);
                        log.info("User creation rolled back successfully for user ID: {}", userToDelete.getId());
                    },
                    () -> log.warn("User with ID {} not found for rollback", userEvent.getUser().getId())
            );
        } catch (Exception e) {
            log.error("Error occurred during user creation rollback: {}", e.getMessage());
        }
    }
}
