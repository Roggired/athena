package ru.yofik.athena.auth.context.user.view;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    public long id;
    public String name;
    public String login;
    public String allowedDeviceId;
    public String createdAt;
    public LockView lockView;
    public InvitationView invitationView;
    public boolean activated;
}
