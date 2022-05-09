package ru.yofik.athena.messenger.infrastructure.integration;

public class AthenaUserCredentials extends AthenaClientCredentials {
    public final char[] accessToken;

    public AthenaUserCredentials(char[] clientToken, String deviceId, char[] accessToken) {
        super(clientToken, deviceId);
        this.accessToken = accessToken;
    }
}
