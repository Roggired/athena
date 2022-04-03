package ru.yofik.athena.admin.context.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Invitation {
    private String code;
    private int count;
}
