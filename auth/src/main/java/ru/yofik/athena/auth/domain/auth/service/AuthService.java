package ru.yofik.athena.auth.domain.auth.service;

import ru.yofik.athena.auth.api.rest.auth.requests.AdminSignInRequest;
import ru.yofik.athena.auth.api.rest.auth.requests.ChangeAdminTemporaryPasswordRequest;
import ru.yofik.athena.auth.api.rest.auth.requests.UserSignInRequest;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;
import ru.yofik.athena.auth.domain.auth.model.UserTokenAccess;

public interface AuthService {
    InternalAccess loginAdmin(AdminSignInRequest request) throws PasswordNeedToBeChangedException;
    void changeAdminTemporaryPassword(ChangeAdminTemporaryPasswordRequest request);

    void logout();

    UserTokenAccess loginUser(UserSignInRequest request);
    InternalAccess checkUserAccess(String accessToken);
    UserTokenAccess refreshUserAccess(String refreshToken);

    String getPublicJwksAsJson();
}
