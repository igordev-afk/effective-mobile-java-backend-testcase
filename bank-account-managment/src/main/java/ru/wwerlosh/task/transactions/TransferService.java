package ru.wwerlosh.task.transactions;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.wwerlosh.task.account.BankAccount;
import ru.wwerlosh.task.account.BankAccountDAO;
import ru.wwerlosh.task.client.UserAuthenticationHttpClient;
import ru.wwerlosh.task.validation.InsufficientFundsException;
import ru.wwerlosh.task.validation.InvalidTransferIdException;
import ru.wwerlosh.task.validation.UserMismatchException;

@Service
@Slf4j
public class TransferService {

    private final BankAccountDAO dao;
    private final UserAuthenticationHttpClient httpClient;

    public TransferService(BankAccountDAO dao, UserAuthenticationHttpClient httpClient) {
        this.dao = dao;
        this.httpClient = httpClient;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processTransferOperation(TransferDTO transferDTO, String authHeader) {
        BankAccount fromAccount = dao.findById(transferDTO.getFrom())
                .orElseThrow(() -> new InvalidTransferIdException("Invalid 'from' account ID provided"));

        Long userId = dao.findById(transferDTO.getFrom()).get().getUserId();
        if (!httpClient.isValidJWT(authHeader.substring(7), userId)) {
            throw new UserMismatchException("The user associated with that JWT does not match the provided ID");
        }

        BankAccount toAccount = dao.findById(transferDTO.getTo())
                .orElseThrow(() -> new InvalidTransferIdException("Invalid 'from' account ID provided"));

        BigDecimal transferAmount = transferDTO.getAmount();
        if (fromAccount.getAmount().compareTo(transferAmount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in your account to perform this operation.");
        }

        fromAccount.setAmount(fromAccount.getAmount().subtract(transferAmount));
        toAccount.setAmount(toAccount.getAmount().add(transferAmount));

        dao.save(fromAccount);
        dao.save(toAccount);

        log.info("Transfer completed successfully: {} units transferred from account {} to account {}",
                transferAmount, fromAccount.getId(), toAccount.getId());
    }
}
