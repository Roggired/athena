package ru.yofik.athena.messenger.api.http.user.view;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyUserView {
    public final long id;
    public final String name;
    public final String login;
}