package ru.yofik.athena.auth.context.user.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.context.user.dto.InvitationRedisDto;
import ru.yofik.athena.auth.context.user.model.Invitation;
import ru.yofik.athena.auth.context.user.model.User;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class InvitationFactory {
    private final CodeGenerator codeGenerator;

    public InvitationFactory(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    public Invitation create(int count, User user) {
        return new Invitation(
                codeGenerator.generate(),
                count,
                user.getId()
        );
    }

    public Invitation from(InvitationRedisDto invitationRedisDto) {
        if (invitationRedisDto == null) {
            return null;
        }

        return new Invitation(
                invitationRedisDto.getCode(),
                invitationRedisDto.getCount(),
                invitationRedisDto.getUserId()
        );
    }

    public InvitationRedisDto toRedisDto(Invitation invitation) {
        if (invitation == null) {
            return null;
        }

        return new InvitationRedisDto(
                invitation.getCode(),
                invitation.getCount(),
                invitation.getUserId()
        );
    }
}
