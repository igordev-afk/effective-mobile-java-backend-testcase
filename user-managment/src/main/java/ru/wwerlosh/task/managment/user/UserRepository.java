package ru.wwerlosh.task.managment.user;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByEmailAndId(String email, Long userId);

    boolean existsByLogin(String login);

    @Query("SELECT u FROM User u " +
            "WHERE (:dateOfBirth IS NULL OR u.dateOfBirth > :dateOfBirth) " +
            "AND (:phone IS NULL OR u.phone = :phone) " +
            "AND (:fullName IS NULL OR CONCAT(u.firstName, ' ', u.lastName) LIKE CONCAT(:fullName, '%')) " +
            "AND (:email IS NULL OR u.email = :email)")
    Page<User> findByFilters(String dateOfBirth, String phone, String fullName, String email, Pageable pageable);

    Optional<User> findByEmail(String email);
}
