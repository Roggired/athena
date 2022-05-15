package ru.yofik.athena.messenger.infrastructure.storage.redis.chat;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.domain.chat.model.JoinChatInvitation;
import ru.yofik.athena.messenger.domain.chat.repository.JoinChatInvitationRepository;
import ru.yofik.athena.messenger.infrastructure.storage.redis.chat.factory.JoinChatInvitationFactory;
import ru.yofik.athena.messenger.infrastructure.storage.redis.chat.repository.CrudJoinChatInvitationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JoinChatInvitationRepositoryImpl implements JoinChatInvitationRepository {
    private final CrudJoinChatInvitationRepository crudJoinChatInvitationRepository;
    private final JoinChatInvitationFactory joinChatInvitationFactory;


    public JoinChatInvitationRepositoryImpl(
            CrudJoinChatInvitationRepository crudJoinChatInvitationRepository,
            JoinChatInvitationFactory joinChatInvitationFactory
    ) {
        this.crudJoinChatInvitationRepository = crudJoinChatInvitationRepository;
        this.joinChatInvitationFactory = joinChatInvitationFactory;
    }


    @Override
    public JoinChatInvitation save(JoinChatInvitation joinChatInvitation) {
        return joinChatInvitationFactory.fromEntity(
                crudJoinChatInvitationRepository.save(
                        joinChatInvitationFactory.toEntity(
                                joinChatInvitation
                        )
                )
        );
    }

    @Override
    public List<JoinChatInvitation> getAllByRecipientId(long recipientId) {
        return crudJoinChatInvitationRepository.findAllByRecipientId(recipientId)
                .stream()
                .map(joinChatInvitationFactory::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public JoinChatInvitation getById(String id) {
        return crudJoinChatInvitationRepository.findById(id)
                .map(joinChatInvitationFactory::fromEntity)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteById(String id) {
        crudJoinChatInvitationRepository.deleteById(id);
    }
}
