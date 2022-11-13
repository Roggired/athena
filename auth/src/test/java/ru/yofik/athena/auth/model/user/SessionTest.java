package ru.yofik.athena.auth.model.user;

import org.junit.jupiter.api.Test;
import ru.yofik.athena.auth.domain.user.model.Session;
import ru.yofik.athena.auth.utils.TimeUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionTest {
    @Test
    void sessionCreatesCorrectly() {
        var session = Session.newSession();

        assertEquals(
                0,
                session.getId()
        );

        assertEquals(
                TimeUtils.infinity(),
                session.getLastLoginDate()
        );
    }
}
