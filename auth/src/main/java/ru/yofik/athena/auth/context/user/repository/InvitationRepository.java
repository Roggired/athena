package ru.yofik.athena.auth.context.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.context.user.dto.InvitationRedisDto;

import java.util.Optional;

@Repository
public interface InvitationRepository extends CrudRepository<InvitationRedisDto, String> {
    Optional<InvitationRedisDto> findByUserId(long userId);
}
