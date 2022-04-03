package ru.yofik.athena.admin.context.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserInfo {
    private Long id;
    private String name;
    private String login;
    private Boolean locked;
    private String createdAt;
    private Boolean activated;
}
