package ru.practicum.main.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.main.states.EventSortType;

import java.time.format.DateTimeFormatter;

public class CommonPatterns {
    private CommonPatterns() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static Pageable patternPageable(Integer from, Integer size) {
        return PageRequest.of(from / size, size);
    }

    public static Pageable patternPageableWithSort(Integer from, Integer size, EventSortType sort) {
        if (sort != null) {
            if (sort.equals(EventSortType.EVENT_DATE)) {
                return PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
            } else {
                return PageRequest.of(from / size, size, Sort.by("views").ascending());
            }
        } else {
            return PageRequest.of(from / size, size);
        }
    }
}
