package ru.yofik.athena.auth.domain.auth.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.infrastructure.config.properties.MailServiceProperties;

import java.util.Locale;

@RequiredArgsConstructor
@Service
@Profile(value = {"stage", "prod"})
public class JavaMailSenderMailService implements MailService {
    private final MessageSource messageSource;
    private final MailServiceProperties mailServiceProperties;
    private final JavaMailSender javaMailSender;


    @Override
    public void sendInvitation(User user, String invitationCode) {
        var invitationLink = removeSlashOnBaseUrlEnd(mailServiceProperties.invitationLink) +
                "?invitation=" + invitationCode + "&userId=" + user.getId();

        var message = new SimpleMailMessage();
        message.setFrom(mailServiceProperties.fromEmail);
        message.setTo(user.getEmail());
        message.setSubject(
                getTranslation(
                        messageSource,
                        MailService.SUBJECT_INVITATION_TRANSLATION_KEY,
                        new Locale("ru")
                )
        );
        message.setText(
                String.format(
                        getTranslation(
                                messageSource,
                                MailService.TEXT_INVITATION_TRANSLATION_KEY,
                                new Locale("ru")
                        ),
                        user.getEmail(),
                        invitationLink
                )
        );

        javaMailSender.send(message);
    }

    @Override
    public void sendResetPasswordLinkToUser(User user, String resetCode) {
        var resetPasswordLink = removeSlashOnBaseUrlEnd(mailServiceProperties.resetPasswordUrl) +
                "?code=" + resetCode + "&userId=" + user.getId();

        var message = new SimpleMailMessage();
        message.setFrom(mailServiceProperties.fromEmail);
        message.setTo(user.getEmail());
        message.setSubject(
                getTranslation(
                        messageSource,
                        MailService.SUBJECT_RESET_PASSWORD_TRANSLATION_KEY,
                        new Locale("ru")
                )
        );
        message.setText(
                String.format(
                        getTranslation(
                                messageSource,
                                MailService.TEXT_RESET_PASSWORD_TRANSLATION_KEY,
                                new Locale("ru")
                        ),
                        user.getEmail(),
                        resetPasswordLink
                )
        );

        javaMailSender.send(message);
    }
}
