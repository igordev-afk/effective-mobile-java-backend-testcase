package ru.wwerlosh.task.account;

import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class BankAccountInterestService {

    private final BankAccountDAO dao;
    private final Double RATE_OF_INTEREST;

    public BankAccountInterestService(BankAccountDAO dao,
                                     @Value("${spring.constants.rate-of-interest}") Double RATE_OF_INTEREST) {
        this.RATE_OF_INTEREST = RATE_OF_INTEREST;
        this.dao = dao;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void increaseBalance() {
        log.info("Scheduled balance increasing <<");
        List<BankAccount> accounts = dao.findAll();
        for (BankAccount account : accounts) {
            BigDecimal cur = account.getAmount();
            BigDecimal max = account.getMaxInterestAmount();

            if (cur.compareTo(max) > 0) {
                continue;
            }

            if (cur.compareTo(max) != 0 && cur.multiply(BigDecimal.valueOf(RATE_OF_INTEREST)).compareTo(max) >= 0) {
                account.setAmount(max);
            } else if (cur.compareTo(max) != 0) {
                BigDecimal newAmount = cur.multiply(BigDecimal.valueOf(RATE_OF_INTEREST));
                account.setAmount(newAmount);
            }

            dao.save(account);
        }
    }
}
