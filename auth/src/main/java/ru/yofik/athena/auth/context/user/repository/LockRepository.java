package ru.yofik.athena.auth.context.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.context.user.dto.LockJpaDto;

@Repository
public interface LockRepository extends JpaRepository<LockJpaDto, Long> {
}
