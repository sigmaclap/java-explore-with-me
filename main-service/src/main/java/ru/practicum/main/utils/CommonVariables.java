package ru.practicum.main.utils;

import java.time.format.DateTimeFormatter;

public class CommonVariables {
    private CommonVariables() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
}
