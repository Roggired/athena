package ru.yofik.athena.admin.context.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Lock {
    private String date;
    private String reason;
}
