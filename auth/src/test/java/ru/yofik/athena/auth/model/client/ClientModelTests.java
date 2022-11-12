package ru.yofik.athena.auth.model.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yofik.athena.auth.domain.client.model.Client;

public class ClientModelTests {
    @Test
    public void challengeCorrectly() {
        var client = Client.newClient(
                "123",
                "123456",
                "https://example.com"
        );

        var actual = client.challengeSecret("123456");
        Assertions.assertTrue(actual);
    }
}
