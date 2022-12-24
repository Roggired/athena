package ru.yofik.athena.auth.domain.auth.service.mail;

import org.springframework.context.MessageSource;
import ru.yofik.athena.auth.domain.user.model.User;

import java.util.Locale;

public interface MailService {
    String SUBJECT_INVITATION_TRANSLATION_KEY = "mailService.subject.invitation";
    String TEXT_INVITATION_TRANSLATION_KEY = "mailService.text.invitation";
    String SUBJECT_NEW_USER_REGISTRATION_REQUEST_TRANSLATION_KEY = "mailService.subject.newUserRegistrationRequest";
    String TEXT_NEW_USER_REGISTRATION_REQUEST_TRANSLATION_KEY = "mailService.text.newUserRegistrationRequest";

    void sendInvitation(User user, String invitationCode);
    void sendNotificationAboutNewUserRegistrationRequest(User admin, String newUserEmail);

    default String removeSlashOnBaseUrlEnd(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl;
    }

    default String getTranslation(MessageSource messageSource, String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}
