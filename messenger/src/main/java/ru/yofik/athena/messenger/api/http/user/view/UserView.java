package ru.yofik.athena.messenger.api.http.user.view;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserView {
    public final long id;
    public final String name;
    public final String login;
    public final boolean isOnline;
    public final String lastOnlineTime;
}
