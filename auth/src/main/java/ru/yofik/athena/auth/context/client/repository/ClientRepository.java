package ru.yofik.athena.auth.context.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.context.client.dto.ClientJpaDto;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientJpaDto, Long> {
    Optional<ClientJpaDto> findByName(String name);
}
