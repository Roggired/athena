package ru.yofik.athena.auth.context.user.view;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientUserView {
    public long id;
    public String name;
    public String login;
}
