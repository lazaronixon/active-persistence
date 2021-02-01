
package com.activepersistence.service.connectionadapters;

import com.activepersistence.model.Base;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ofPattern;

public class Quoting {

    public static final DateTimeFormatter DATE_FORMAT      = ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMAT      = ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

    public static String quote(Object value) {
        if (value instanceof Base) {
            return _quote(idValueForDatabase((Base) value));
        } else {
            return _quote(value);
        }
    }

    private static Object idValueForDatabase(Base value) {
        return value.getId();
    }

    private static String _quote(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof String) {
            return quote((String) value);
        } else if (value instanceof Long) {
            return quote((Long) value);
        } else if (value instanceof Float) {
            return quote((Float) value);
        } else if (value instanceof Double) {
            return quote((Double) value);
        } else if (value instanceof Number) {
            return quote((Number) value);
        } else if (value instanceof Boolean) {
            return quote((Boolean) value);
        } else if (value instanceof LocalDate) {
            return quote((LocalDate) value);
        } else if (value instanceof LocalTime) {
            return quote((LocalTime) value);
        } else if (value instanceof LocalDateTime) {
            return quote((LocalDateTime) value);
        } else if (value instanceof Enum) {
            return quote((Enum) value);
        } else if (value instanceof Class) {
            return quote((Class) value);
        } else {
            throw new RuntimeException("can't quote: " + value.getClass().getName());
        }
    }

    private static String quote(String value) {
        return "'" + value.replaceAll("'", "''") + "'";
    }

    private static String quote(Boolean value) {
        return value ? "TRUE" : "FALSE";
    }

    private static String quote(Long value) {
        return value + "L";
    }

    private static String quote(Float value) {
        return value + "F";
    }

    private static String quote(Double value) {
        return value + "D";
    }

    private static String quote(Number value) {
        return value.toString();
    }

    private static String quote(LocalDate value) {
        return "{d '" + value.format(DATE_FORMAT) + "'}";
    }

    private static String quote(LocalTime value) {
        return "{t '" + value.format(TIME_FORMAT) + "'}";
    }

    private static String quote(LocalDateTime value) {
        return "{ts '" + value.format(DATE_TIME_FORMAT) + "'}";
    }

    private static String quote(Class value) {
        return value.getSimpleName();
    }

    private static String quote(Enum value) {
        return value.getClass().getName() + "." + value.name();
    }

}
