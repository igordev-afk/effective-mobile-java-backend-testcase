package ru.wwerlosh.task.transactions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import ru.wwerlosh.task.validation.GreaterOrEqualsTo;

@Data
public class TransferDTO {

    @NotNull(message = "The 'from' field must not be empty")
    @NotEmpty(message = "The 'from' field must not be empty")
    private Long from;

    @NotNull(message = "The 'to' field must not be empty")
    @NotEmpty(message = "The 'to' field must not be empty")
    private Long to;

    @NotNull(message = "The 'amount' field must not be empty")
    @NotEmpty(message = "The 'to' field must not be empty")
    @GreaterOrEqualsTo(value = "50")
    private BigDecimal amount;

    public TransferDTO(Long from, Long to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
