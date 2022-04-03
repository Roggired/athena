package ru.yofik.athena.admin.context.user.integration.auth.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewInvitationAuthResponse {
    public String code;
    public int count;
}
