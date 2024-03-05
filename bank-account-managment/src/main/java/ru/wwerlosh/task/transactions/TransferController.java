package ru.wwerlosh.task.transactions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.task.account.BankAccountDAO;
import ru.wwerlosh.task.client.UserAuthenticationHttpClient;

@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;}

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferDTO transferDTO, HttpServletRequest hsr) {
        String authHeader = hsr.getHeader("Authorization");
        if (authHeader.length() < 8) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        transferService.processTransferOperation(transferDTO, authHeader);
        return ResponseEntity.ok().body("The transaction was completed successfully.");
    }
}
