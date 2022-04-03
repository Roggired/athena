package ru.yofik.athena.auth.context.user.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.context.user.dto.InvitationRedisDto;
import ru.yofik.athena.auth.context.user.model.Invitation;
import ru.yofik.athena.auth.context.user.model.User;

@Component
public class InvitationFactory {
    public Invitation create(String code, int count, User user) {
        return new Invitation(
                code,
                count,
                user.getId()
        );
    }

    public Invitation from(InvitationRedisDto invitationRedisDto) {
        return new Invitation(
                invitationRedisDto.getCode(),
                invitationRedisDto.getCount(),
                invitationRedisDto.getUserId()
        );
    }

    public InvitationRedisDto toRedisDto(Invitation invitation) {
        return new InvitationRedisDto(
                invitation.getCode(),
                invitation.getCount(),
                invitation.getUserId()
        );
    }
}
