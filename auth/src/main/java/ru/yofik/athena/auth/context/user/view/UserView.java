package ru.yofik.athena.auth.context.user.view;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserView {
    public long id;
    public String name;
    public String login;
    public String allowedDeviceId;
    public String createdAt;
    public LockView lock;
    public InvitationView invitation;
    public boolean activated;
}
