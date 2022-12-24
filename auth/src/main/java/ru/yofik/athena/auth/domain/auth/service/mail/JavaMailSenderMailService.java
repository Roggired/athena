package ru.yofik.athena.auth.domain.auth.service.mail;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.infrastructure.config.properties.MailServiceProperties;

import java.util.Locale;

@Service
@Profile(value = {"prod"})
public class JavaMailSenderMailService implements MailService {
    private final MessageSource messageSource;
    private final MailServiceProperties mailServiceProperties;
    private final JavaMailSender javaMailSender;

    public JavaMailSenderMailService(MessageSource messageSource, MailServiceProperties mailServiceProperties, JavaMailSender javaMailSender) {
        this.messageSource = messageSource;
        this.mailServiceProperties = mailServiceProperties;
        this.javaMailSender = javaMailSender;
    }

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
                        invitationLink,
                        user.getId(),
                        invitationCode
                )
        );

        javaMailSender.send(message);
    }

    @Override
    public void sendNotificationAboutNewUserRegistrationRequest(User admin, String newUserEmail) {
        var message = new SimpleMailMessage();
        message.setFrom(mailServiceProperties.fromEmail);
        message.setTo(admin.getEmail());
        message.setSubject(
                getTranslation(
                        messageSource,
                        MailService.SUBJECT_NEW_USER_REGISTRATION_REQUEST_TRANSLATION_KEY,
                        new Locale("ru")
                )
        );
        message.setText(
                String.format(
                        getTranslation(
                                messageSource,
                                MailService.TEXT_NEW_USER_REGISTRATION_REQUEST_TRANSLATION_KEY,
                                new Locale("ru")
                        ),
                        newUserEmail
                )
        );

        javaMailSender.send(message);
    }
}
