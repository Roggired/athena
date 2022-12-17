package ru.yofik.athena.auth.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

public final class TimeUtils {
    public static LocalDateTime infinity() {
        return LocalDateTime.of(2200, Month.JANUARY, 1, 0, 0);
    }

    public static LocalDateTime nowUTC() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static LocalDateTime UTCTime(Long epochMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of("UTC"));
    }
}
