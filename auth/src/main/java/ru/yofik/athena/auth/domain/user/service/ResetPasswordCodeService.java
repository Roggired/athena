package ru.yofik.athena.auth.domain.user.service;

import ru.yofik.athena.auth.domain.user.model.ResetPasswordCode;
import ru.yofik.athena.auth.domain.user.model.User;

public interface ResetPasswordCodeService {
    ResetPasswordCode getOrCreateForAdmin(User user);
    ResetPasswordCode getByCode(String code);
    void markAsUsed(ResetPasswordCode resetPasswordCode);
}
