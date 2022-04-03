package ru.yofik.athena.auth.context.user.view;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LockView {
    public String date;
    public String reason;
}
