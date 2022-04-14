package ru.yofik.athena.admin.context.user.api.response;

import lombok.AllArgsConstructor;
import ru.yofik.athena.admin.context.user.model.UserInfo;

import java.util.List;

@AllArgsConstructor
public class GetAllUsersResponse {
    public List<UserInfo> users;
}
