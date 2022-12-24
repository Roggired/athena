package ru.yofik.athena.auth.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.domain.auth.service.code.CodeGenerator;
import ru.yofik.athena.auth.domain.auth.service.mail.MailService;
import ru.yofik.athena.auth.domain.user.model.User;

@RequiredArgsConstructor
@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {
    private final MailService mailService;
    private final CodeGenerator codeGenerator;


    @Override
    public String inviteUser(User user) {
        var invitation = inviteUserWithoutEmailNotification(user);
        mailService.sendInvitation(user, invitation);
        return invitation;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String inviteUserWithoutEmailNotification(User user) {
        var invitation = codeGenerator.generateShort();
        user.getCredentials().changeUserCredentials(invitation);
        return invitation;
    }
}
