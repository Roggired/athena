package ru.yofik.athena.admin.context.client.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.HashMap;
import java.util.Map;

@ApplicationScope
@Component
public class AdminKeyStorage {
    private final Map<String, char[]> sessionMap = new HashMap<>();

    public synchronized void add(String session, char[] token) {
        sessionMap.put(session, token);
    }

    public synchronized void remove(String session) {
        sessionMap.remove(session);
    }

    public synchronized char[] get(String session) {
        if (!sessionMap.containsKey(session)) {
            return new char[0];
        }

        return sessionMap.get(session);
    }
}
