package ru.yofik.athena.auth.domain.auth.service;

import ru.yofik.athena.auth.api.rest.admin.requests.ApproveUserRegistrationRequest;
import ru.yofik.athena.auth.api.rest.admin.requests.FilteredUserRegistrationRequest;
import ru.yofik.athena.auth.domain.auth.model.UserRegistrationRequest;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.common.domain.NewPage;

public interface UserRegistrationRequestService {
    User approve(ApproveUserRegistrationRequest request);
    NewPage<UserRegistrationRequest> getPage(NewPage.Meta pageMeta, FilteredUserRegistrationRequest request);
    void reject(long requestId);
}
