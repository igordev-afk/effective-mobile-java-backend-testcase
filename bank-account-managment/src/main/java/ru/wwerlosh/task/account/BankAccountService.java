package ru.wwerlosh.task.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankAccountService {

    private final BankAccountDAO dao;
    private final KafkaTemplate<String, UserCreationEvent> userCreationEventKafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Double MAX_RATE_OF_INTEREST;

    public BankAccountService(BankAccountDAO dao,
                              KafkaTemplate<String, UserCreationEvent> userCreationEventKafkaTemplate,
                              ObjectMapper objectMapper,
                              @Value("${spring.constants.max-rate-of-interest}") Double MAX_RATE_OF_INTEREST) {
        this.userCreationEventKafkaTemplate = userCreationEventKafkaTemplate;
        this.objectMapper = objectMapper;
        this.MAX_RATE_OF_INTEREST = MAX_RATE_OF_INTEREST;
        this.dao = dao;
    }

    @KafkaListener(topics = "new-users")
    public void createBankAccount(String event) {

        UserCreationEvent userCreationEvent;
        try {
            userCreationEvent = objectMapper.readValue(event, UserCreationEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        UserRegistrationDTO user = userCreationEvent.getUser();

        try {

            BankAccount bankAccount = new BankAccount();
            bankAccount.setAmount(user.getAmount());
            bankAccount.setMaxInterestAmount(
                    user.getAmount().multiply(BigDecimal.valueOf(MAX_RATE_OF_INTEREST)));
            bankAccount.setUserId(user.getId());
            dao.save(bankAccount);

            log.info("Bank account created successfully for user: {}", user.getId());

        } catch (Exception e) {

            log.error("Failed to create bank account for user: {}", user.getId(), e);

            UserCreationEvent ucr = new UserCreationEvent();
            userCreationEvent.setType("CREATION FAILED");
            userCreationEvent.setUser(user);

            userCreationEventKafkaTemplate.send("rollback-user-creation", ucr);
        }

    }
}
