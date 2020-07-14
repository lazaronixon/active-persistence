package com.activepersistence.service.relation;

import com.activepersistence.PreparedStatementInvalid;
import com.activepersistence.service.Relation;
import static java.lang.String.format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;

public class WhereClauseFactory {

    private final String statement;

    private final Object[] values;

    public WhereClauseFactory(String statement, Object[] values) {
        this.statement = statement;
        this.values    = values;
    }

    public String build() {
        if (values.length > 0) {
            if (values[0] instanceof Map && compile(":\\w+").matcher(statement).find()) {
                return replaceNamedBindVariables(statement, (Map) values[0]);
            } else if (statement.contains("?")) {
                return replaceBindVariables(statement, values);
            } else if (statement.isBlank()) {
                return statement;
            } else {
                return format(statement, asList(values).stream().map(this::literalize).toArray());
            }
        } else {
            return statement;
        }
    }

    private String replaceNamedBindVariables(String statement, Map<String, Object> bindVars) {
        return compile("(:?):([a-zA-Z]\\w*)").matcher(statement).replaceAll(match -> {
            if (match.group(1).equals(":")) {
                return match.group();
            } else if (bindVars.keySet().contains(match.group(2))) {
                return replaceBindVariable(bindVars.get(match.group(2)));
            } else {
                throw new PreparedStatementInvalid("missing value for :" + match + " in " + statement);
            }
        });
    };

    private String replaceBindVariables(String statement, Object[] values) {
        int bindCount    = (int) statement.chars().filter(ch -> ch == '?').count();
        int valuesCount  = (int) values.length;

        if (bindCount == valuesCount) {
            List<Object> bound = new ArrayList(asList(values));
            return compile("\\?").matcher(statement).replaceAll(match -> quoteReplacement(replaceBindVariable(bound.remove(0))));
        } else {
            throw new PreparedStatementInvalid("wrong number of bind variables (" + bindCount + " for " + valuesCount + ") in: " + statement);
        }
    };

    private String replaceBindVariable(Object value) {
        if (value instanceof Relation) {
            return ((Relation) value).toJpql();
        } else {
            return literalizeBoundValue(value);
        }
    }

    private String literalizeBoundValue(Object value) {
        if (value instanceof List) {
            Supplier<Stream<String>> literalized = () -> ((List)value).stream().map(this::literalize);
            if (literalized.get().findAny().isEmpty()) {
                return literalize((Object) null);
            } else {
                return literalized.get().collect(joining(","));
            }
        } else {
            return literalize(value);
        }
    }

    private String literalize(Object value) {
        if (value == null)                  return "NULL";
        if (value instanceof Enum)          return literalize((Enum) value);
        if (value instanceof String)        return literalize((String) value);
        if (value instanceof Integer)       return literalize((Integer) value);
        if (value instanceof Long)          return literalize((Long) value);
        if (value instanceof Float)         return literalize((Float) value);
        if (value instanceof Double)        return literalize((Double) value);
        if (value instanceof Boolean)       return literalize((Boolean) value);
        if (value instanceof LocalDate)     return literalize((LocalDate) value);
        if (value instanceof LocalTime)     return literalize((LocalTime) value);
        if (value instanceof LocalDateTime) return literalize((LocalDateTime) value);

        throw new PreparedStatementInvalid("type not supported for binding: " + value.getClass().getName());
    }

    private String literalize(Enum value) {
        return value.getClass().getName() + "." + value.name();
    }

    private String literalize(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    private String literalize(Integer value) {
        return value.toString();
    }

    private String literalize(Long value) {
        return value + "L";
    }

    private String literalize(Float value) {
        return value + "F";
    }

    private String literalize(Double value) {
        return value + "D";
    }

    private String literalize(Boolean value) {
        return value ? "TRUE" : "FALSE";
    }

    private String literalize(LocalDate value) {
        return "{d'" + value + "'}";
    }

    private String literalize(LocalTime value) {
        return "{t'" + value + "'}";
    }

    private String literalize(LocalDateTime value) {
        return "{ts'" + value.toString().replace("T", " ") + "'}";
    }

}
