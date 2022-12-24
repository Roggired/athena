package ru.yofik.athena.auth.domain.auth.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.infrastructure.config.properties.MailServiceProperties;

import java.util.Locale;

@RequiredArgsConstructor
@Component
@Profile(value = {"stage", "dev"})
@Slf4j
public class DevAndStageMailService implements MailService {
    private final MessageSource messageSource;
    private final MailServiceProperties mailServiceProperties;


    @Override
    public void sendInvitation(User user, String invitationCode) {
        var invitationLink = removeSlashOnBaseUrlEnd(mailServiceProperties.invitationLink) +
                "?invitation=" + invitationCode + "&userId=" + user.getId();

        log.info(
                "Invitation code: " + invitationCode + " encoded in the invitation link " + invitationLink + " " +
                "has been 'sent' for user: " + user.getEmail() + System.lineSeparator() +
                "Full information about the message: " + System.lineSeparator() +
                "From: " + mailServiceProperties.fromEmail + System.lineSeparator() +
                "To: " + user.getEmail() + System.lineSeparator() +
                "Subject: " + getTranslation(messageSource, MailService.SUBJECT_INVITATION_TRANSLATION_KEY, new Locale("ru")) + System.lineSeparator() +
                "Text: " + String.format(
                        getTranslation(
                                messageSource,
                                MailService.TEXT_INVITATION_TRANSLATION_KEY,
                                new Locale("ru")
                        ),
                        user.getEmail(),
                        invitationLink,
                        user.getId(),
                        invitationCode
                )
        );
    }

    @Override
    public void sendNotificationAboutNewUserRegistrationRequest(User admin, String newUserEmail) {
        log.info(
                "Notification about new user registration request" +
                        "has been 'sent' for user: " + admin.getEmail() + System.lineSeparator() +
                        "Full information about the message: " + System.lineSeparator() +
                        "From: " + mailServiceProperties.fromEmail + System.lineSeparator() +
                        "To: " + admin.getEmail() + System.lineSeparator() +
                        "Subject: " + getTranslation(messageSource, MailService.SUBJECT_NEW_USER_REGISTRATION_REQUEST_TRANSLATION_KEY, new Locale("ru")) + System.lineSeparator() +
                        "Text: " + String.format(
                        getTranslation(
                                messageSource,
                                MailService.TEXT_NEW_USER_REGISTRATION_REQUEST_TRANSLATION_KEY,
                                new Locale("ru")
                        ),
                        newUserEmail
                )
        );
    }
}
