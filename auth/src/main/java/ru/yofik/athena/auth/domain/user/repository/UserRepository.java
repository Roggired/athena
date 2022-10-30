package ru.yofik.athena.auth.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    @Query(
            value = "SELECT u FROM User u " +
                    "WHERE (COALESCE(:login, NULL) IS NULL OR u.login LIKE :login || '%') AND " +
                    "(COALESCE(:allowedDeviceId, NULL) IS NULL OR u.session.allowedDeviceId LIKE :allowedDeviceId || '%') AND " +
                    "(COALESCE(:role, NULL) IS NULL OR :role = u.role)"
    )
    Page<User> findAllByFilters(
            String login,
            String allowedDeviceId,
            Role role,
            Pageable pageable
    );
}
