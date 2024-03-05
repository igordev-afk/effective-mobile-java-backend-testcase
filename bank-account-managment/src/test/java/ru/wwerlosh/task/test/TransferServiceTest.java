package ru.wwerlosh.task.test;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.wwerlosh.task.account.BankAccount;
import ru.wwerlosh.task.account.BankAccountDAO;
import ru.wwerlosh.task.client.UserAuthenticationHttpClient;
import ru.wwerlosh.task.transactions.TransferDTO;
import ru.wwerlosh.task.transactions.TransferService;
import ru.wwerlosh.task.validation.InvalidTransferIdException;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferServiceTest {

    @Autowired
    private BankAccountDAO bankAccountDAO;

    private TransferService transferService;

    @Test
    public void processTransferOperation_test() {

        UserAuthenticationHttpClient userAuthenticationHttpClient = mock(UserAuthenticationHttpClient.class);
        when(userAuthenticationHttpClient.isValidJWT(anyString(), anyLong())).thenReturn(true);
        transferService = new TransferService(bankAccountDAO, userAuthenticationHttpClient);
        TransferDTO transferDTO = new TransferDTO(1L, 2L, BigDecimal.valueOf(1000));

        transferService.processTransferOperation(transferDTO, "mock_valid_jwt");

        Optional<BankAccount> fromAccountOptional = bankAccountDAO.findById(1L);
        Optional<BankAccount> toAccountOptional = bankAccountDAO.findById(2L);

        assertTrue(fromAccountOptional.isPresent());
        assertTrue(toAccountOptional.isPresent());

        BankAccount fromAccount = fromAccountOptional.get();
        BankAccount toAccount = toAccountOptional.get();

        assertEquals(BigDecimal.valueOf(21516.75), fromAccount.getAmount());
        assertEquals(BigDecimal.valueOf(3000), toAccount.getAmount());
    }

}
