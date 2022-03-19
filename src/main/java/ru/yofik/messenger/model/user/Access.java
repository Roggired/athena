package ru.yofik.messenger.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Access {
    private final String accessToken;
}
