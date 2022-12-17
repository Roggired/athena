package ru.yofik.athena.auth.domain.auth.service.mail;

import org.springframework.context.MessageSource;
import ru.yofik.athena.auth.domain.user.model.User;

import java.util.Locale;

public interface MailService {
    String SUBJECT_INVITATION_TRANSLATION_KEY = "mailService.subject.registration";
    String TEXT_INVITATION_TRANSLATION_KEY = "mailService.text.registration";
    String SUBJECT_RESET_PASSWORD_TRANSLATION_KEY = "mailService.subject.resetPassword";
    String TEXT_RESET_PASSWORD_TRANSLATION_KEY = "mailService.text.resetPassword";

    void sendInvitation(User user, String invitationCode);
    void sendResetPasswordLinkToUser(User user, String resetCode);

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
