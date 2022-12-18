package ru.yofik.athena.auth.api.rest.user.requests;

import lombok.NoArgsConstructor;
import ru.yofik.athena.auth.domain.user.model.Role;

@NoArgsConstructor
public class FilteredUsersRequest {
    public String login;
    public Role role;
}
