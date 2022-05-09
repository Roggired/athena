package ru.yofik.athena.messenger.infrastructure.integration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AthenaClientCredentials {
    public final char[] clientToken;
    public final String deviceId;
}
