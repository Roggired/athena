package ru.yofik.athena.auth.domain.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.domain.auth.model.UserRegistrationRequest;

@Repository
public interface UserRegistrationRequestRepository extends JpaRepository<UserRegistrationRequest, Long> {
    @Query(
            value = "SELECT r FROM UserRegistrationRequest r " +
                    "WHERE (COALESCE(:email, NULL) IS NULL OR r.email LIKE CONCAT(CAST(:email AS text), '%'))"
    )
    Page<UserRegistrationRequest> findAllByEmail(
            String email,
            Pageable pageable
    );
}
