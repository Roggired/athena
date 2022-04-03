package ru.yofik.athena.auth.context.user.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.context.user.dto.UserJpaDto;
import ru.yofik.athena.auth.context.user.model.User;
import ru.yofik.athena.auth.context.user.repository.InvitationRepository;

import java.time.ZonedDateTime;

@Component
public class UserFactory {
    private final InvitationRepository invitationRepository;
    private final InvitationFactory invitationFactory;
    private final LockFactory lockFactory;

    public UserFactory(InvitationRepository invitationRepository, InvitationFactory invitationFactory, LockFactory lockFactory) {
        this.invitationRepository = invitationRepository;
        this.invitationFactory = invitationFactory;
        this.lockFactory = lockFactory;
    }

    public User createNew(String name,
                          String login) {

        return new User(
                0,
                name,
                login,
                null,
                ZonedDateTime.now()
        );
    }

    public User from(UserJpaDto userJpaDto) {
        var invitation = invitationRepository.findByUserId(userJpaDto.getId());
        return new User(
                userJpaDto.getId(),
                userJpaDto.getName(),
                userJpaDto.getLogin(),
                lockFactory.from(userJpaDto.getLock()),
                userJpaDto.getAllowedDeviceId (),
                userJpaDto.getCreatedAt(),
                invitation.map(invitationFactory::from).orElse(null),
                userJpaDto.isActivated()
        );
    }

    public UserJpaDto toUserJpaDto(User user) {
        return new UserJpaDto(
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.isLocked() ? lockFactory.toJpaDto(user.getLock()) : null,
                user.getAllowedDeviceId(),
                user.getCreatedAt(),
                user.isActivated()
        );
    }
}
