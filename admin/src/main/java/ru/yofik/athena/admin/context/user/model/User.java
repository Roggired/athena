package ru.yofik.athena.admin.context.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String name;
    private String login;
    private String allowedDeviceId;
    private String createdAt;
    private Lock lock;
    private Invitation invitation;
    private Boolean activated;
}
