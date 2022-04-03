package ru.yofik.athena.auth.context.user.view;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserShortView {
    public long id;
    public String name;
    public String login;
    public boolean locked;
    public String createdAt;
    public boolean activated;
}
