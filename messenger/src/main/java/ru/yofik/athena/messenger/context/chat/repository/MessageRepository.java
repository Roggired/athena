package ru.yofik.athena.messenger.context.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.messenger.context.chat.dto.MessageJpaDto;

@Repository
public interface MessageRepository extends JpaRepository<MessageJpaDto, Long> {
}
