package ru.wwerlosh.task.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.wwerlosh.task.transactions.TransferController;
import ru.wwerlosh.task.transactions.TransferDTO;
import ru.wwerlosh.task.transactions.TransferService;
import ru.wwerlosh.task.validation.InsufficientFundsException;
import ru.wwerlosh.task.validation.InvalidTransferIdException;
import ru.wwerlosh.task.validation.UserMismatchException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @Test
    public void testSuccessfulTransfer() throws Exception {
        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content("{\"from\": 123, \"to\": 456, \"amount\": 100}")
                        .header("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isOk());
    }

    @Test
    public void testInsufficientFundsException() throws Exception {
        doThrow(InsufficientFundsException.class)
                .when(transferService)
                .processTransferOperation(any(TransferDTO.class), any(String.class));

        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content("{\"from\": 123, \"to\": 456, \"amount\": 100}")
                        .header("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidTransferIdException() throws Exception {
        doThrow(InvalidTransferIdException.class)
                .when(transferService)
                .processTransferOperation(any(TransferDTO.class), any(String.class));

        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content("{\"from\": 123, \"to\": 456, \"amount\": 100}")
                        .header("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserMismatchException() throws Exception {
        doThrow(UserMismatchException.class)
                .when(transferService)
                .processTransferOperation(any(TransferDTO.class), any(String.class));

        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content("{\"from\": 123, \"to\": 456, \"amount\": 100}")
                        .header("Authorization", "Bearer valid_jwt_token"))
                .andExpect(status().isUnauthorized());
    }
}
