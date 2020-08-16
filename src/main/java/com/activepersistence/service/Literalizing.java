
package com.activepersistence.service;

import com.activepersistence.model.Base;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ofPattern;

public class Literalizing {

    private static final DateTimeFormatter DATE_FORMAT      = ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT      = ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

    public static String literal(Object value) {
        if (value instanceof Base) {
            return _literal(idValueForDatabase((Base) value));
        } else {
            return _literal(value);
        }
    }

    private static Object idValueForDatabase(Base value) {
        return value.getId();
    }

    private static String _literal(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof String) {
            return _literal((String) value);
        } else if (value instanceof Long) {
            return _literal((Long) value);
        } else if (value instanceof Float) {
            return _literal((Float) value);
        } else if (value instanceof Double) {
            return _literal((Double) value);
        } else if (value instanceof Number) {
            return _literal((Number) value);
        } else if (value instanceof Boolean) {
            return _literal((Boolean) value);
        } else if (value instanceof LocalDate) {
            return _literal((LocalDate) value);
        } else if (value instanceof LocalTime) {
            return _literal((LocalTime) value);
        } else if (value instanceof LocalDateTime) {
            return _literal((LocalDateTime) value);
        } else if (value instanceof Enum) {
            return _literal((Enum) value);
        } else if (value instanceof Class) {
            return _literal((Class) value);
        } else {
            throw new RuntimeException("can't literalize: " + value.getClass().getName());
        }
    }

    private static String _literal(String value) {
        return "'" + value.replaceAll("'", "''") + "'";
    }

    private static String _literal(Boolean value) {
        return value ? "TRUE" : "FALSE";
    }

    private static String _literal(Long value) {
        return value + "L";
    }

    private static String _literal(Float value) {
        return value + "F";
    }

    private static String _literal(Double value) {
        return value + "D";
    }

    private static String _literal(Number value) {
        return value.toString();
    }

    private static String _literal(LocalDate value) {
        return "{d '" + value.format(DATE_FORMAT) + "'}";
    }

    private static String _literal(LocalTime value) {
        return "{t '" + value.format(TIME_FORMAT) + "'}";
    }

    private static String _literal(LocalDateTime value) {
        return "{ts '" + value.format(DATE_TIME_FORMAT) + "'}";
    }

    private static String _literal(Class value) {
        return value.getSimpleName();
    }

    private static String _literal(Enum value) {
        return value.getClass().getName() + "." + value.name();
    }

}
