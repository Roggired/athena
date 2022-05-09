package ru.yofik.athena.messenger.api.ws;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationSubscriptionKey implements SubscriptionKey {
    @EqualsAndHashCode.Include
    private final long userId;

    @Override
    public String toStringKey() {
        return userId + "";
    }
}
