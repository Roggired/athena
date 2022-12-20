package ru.yofik.athena.messenger.infrastructure.integration.auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilteredUsersRequest {
    public final String login;
    public final String role;
}
