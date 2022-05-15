package ru.yofik.athena.messenger.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateUtils {
    public static LocalDateTime nowUTC() {
        return Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }
}
