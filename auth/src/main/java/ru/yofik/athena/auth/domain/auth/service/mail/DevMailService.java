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
@Profile("dev")
@Slf4j
public class DevMailService implements MailService {
    private final MessageSource messageSource;
    private final MailServiceProperties mailServiceProperties;


    @Override
    public void sendInvitation(User user, String invitationCode) {
        var invitationLink = removeSlashOnBaseUrlEnd(mailServiceProperties.invitationLink) +
                "?invitation=" + invitationCode + "&userId=" + user.getId();

        log.debug(
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
                        invitationLink
                )
        );
    }

    @Override
    public void sendResetPasswordLinkToUser(User user, String resetCode) {
        var resetPasswordLink = removeSlashOnBaseUrlEnd(mailServiceProperties.resetPasswordUrl) +
                "?code=" + resetCode + "&userId=" + user.getId();

        log.debug(
                "Reset password code: " + resetCode + " encoded in the reset password link: " + resetPasswordLink +
                        "if in corresponding user object no reset password object" + System.lineSeparator() +
                        "Full information about the message: " + System.lineSeparator() +
                        "From: " + mailServiceProperties.fromEmail + System.lineSeparator() +
                        "To: " + user.getEmail() + System.lineSeparator() +
                        "Subject: " + getTranslation(messageSource, MailService.SUBJECT_RESET_PASSWORD_TRANSLATION_KEY, new Locale("ru")) + System.lineSeparator() +
                        "Text: " + String.format(
                                getTranslation(
                                        messageSource,
                                        MailService.TEXT_RESET_PASSWORD_TRANSLATION_KEY,
                                        new Locale("ru")
                                ),
                                user.getEmail(),
                                resetPasswordLink
                        )
        );
    }
}
