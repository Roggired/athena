package ru.yofik.messenger.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessView {
    private final String accessToken;
}
