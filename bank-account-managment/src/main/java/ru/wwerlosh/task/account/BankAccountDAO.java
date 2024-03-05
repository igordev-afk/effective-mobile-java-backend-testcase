package ru.wwerlosh.task.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class BankAccountDAO {
    private final JdbcTemplate jdbcTemplate;

    public BankAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<BankAccount> findById(Long id) {
        String sql = "SELECT * FROM bank_account_table WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) ->
                new BankAccount(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("max_interest_amount"),
                        rs.getInt("version")
                )).stream().findFirst();
    }

    public List<BankAccount> findAll() {
        String sql = "SELECT * FROM bank_account_table";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new BankAccount(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("max_interest_amount"),
                        rs.getInt("version")
                ));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void save(BankAccount bankAccount) {
        if (bankAccount.getId() == null) {
            String sql = "INSERT INTO bank_account_table (user_id, amount, max_interest_amount, version) " +
                    "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    bankAccount.getUserId(),
                    bankAccount.getAmount(),
                    bankAccount.getMaxInterestAmount(),
                    1);
        } else {
            String sql = "UPDATE bank_account_table SET amount = ?, max_interest_amount = ?, version = ? " +
                    "WHERE id = ? AND version = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    bankAccount.getAmount(),
                    bankAccount.getMaxInterestAmount(),
                    bankAccount.getVersion() + 1,
                    bankAccount.getId(),
                    bankAccount.getVersion());
            if (rowsAffected == 0) {
                log.warn("Optimistic locking failed for BankAccount with ID: {}", bankAccount.getId());
            }
        }
    }
}
