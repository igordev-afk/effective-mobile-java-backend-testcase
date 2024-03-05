package ru.wwerlosh.task.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
@Table(name = "bank_account_table")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal maxInterestAmount;

    @Version
    private Integer version;

    public BankAccount(Long userId, BigDecimal amount, BigDecimal maxInterestAmount, Integer version) {
        this.userId = userId;
        this.amount = amount;
        this.maxInterestAmount = maxInterestAmount;
        this.version = version;
    }

    public BankAccount(Long id, Long userId, BigDecimal amount, BigDecimal maxInterestAmount, Integer version) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.maxInterestAmount = maxInterestAmount;
        this.version = version;
    }

    public BankAccount() {
    }
}
