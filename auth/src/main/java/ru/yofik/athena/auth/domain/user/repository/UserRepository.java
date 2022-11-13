package ru.yofik.athena.auth.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.domain.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = "SELECT u FROM User u " +
                    "LEFT JOIN FETCH u.credentials creds " +
                    "LEFT JOIN FETCH u.lock lock " +
                    "LEFT JOIN FETCH u.session session " +
                    "WHERE u.login = :login"
    )
    Optional<User> findByLogin(String login);

    @Query(
            value = "SELECT u FROM User u " +
                    "LEFT JOIN FETCH u.credentials creds " +
                    "LEFT JOIN FETCH u.lock lock " +
                    "LEFT JOIN FETCH u.session session " +
                    "WHERE (COALESCE(:login, NULL) IS NULL OR u.login LIKE CONCAT(CAST(:login AS text), '%')) AND " +
                    "(COALESCE(:role, NULL) IS NULL OR u.role = CAST(:role AS text))",
            countQuery = "SELECT COUNT(u) FROM User u " +
                    "WHERE (COALESCE(:login, NULL) IS NULL OR u.login LIKE CONCAT(CAST(:login AS text), '%')) AND " +
                    "(COALESCE(:role, NULL) IS NULL OR u.role = CAST(:role AS text))"
    )
    Page<User> findAllByFilters(
            String login,
            String role,
            Pageable pageable
    );
}
