package ru.yofik.athena.auth.utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

public final class TimeUtils {
    public static LocalDateTime infinity() {
        return LocalDateTime.of(2200, Month.JANUARY, 1, 0, 0);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
