package ru.dto.dtos.utils;


import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
}
