package ru.yofik.athena.auth.context.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.context.user.dto.UserJpaDto;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserJpaDto, Long> {
    Optional<UserJpaDto> findByLogin(String login);
}
