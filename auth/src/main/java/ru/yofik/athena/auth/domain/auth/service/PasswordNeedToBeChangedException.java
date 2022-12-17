package ru.yofik.athena.auth.domain.auth.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yofik.athena.auth.domain.auth.model.AdminChangePasswordResponse;

@RequiredArgsConstructor
@Getter
public class PasswordNeedToBeChangedException extends Exception {
    private final AdminChangePasswordResponse adminChangePasswordResponse;
}
