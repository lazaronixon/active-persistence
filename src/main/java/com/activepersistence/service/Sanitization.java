package com.activepersistence.service;

import com.activepersistence.PreparedStatementInvalid;
import static com.activepersistence.service.connectionadapters.Literalizing.literal;
import static com.activepersistence.service.relation.ValueMethods.CONSTRUCTOR;
import static java.lang.String.format;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;

public class Sanitization {

    public static String sanitizeJpql(String statement, Object... values) {
        if (statement.isEmpty()) return null;

        if (values != null && values.length > 0) {
            if (values[0] instanceof Map && compile(":\\w+").matcher(statement).find()) {
                return replaceNamedBindVariables(statement, (Map) values[0]);
            } else if (statement.contains("?")) {
                return replaceBindVariables(statement, values);
            } else if (statement.isBlank()) {
                return statement;
            } else {
                return format(statement, stream(values).map(v -> literal(v)).toArray());
            }
        } else {
            return statement;
        }
    }

    private static String replaceNamedBindVariables(String statement, Map<String, Object> bindVars) {
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

    private static String replaceBindVariables(String statement, Object[] values) {
        var bindCount    = (int) statement.chars().filter(ch -> ch == '?').count();
        var valuesCount  = (int) values.length;

        if (bindCount == valuesCount) {
            var bound = new ArrayList(asList(values)); return compile("\\?").matcher(statement).replaceAll(match -> quoteReplacement(replaceBindVariable(bound.remove(0))));
        } else {
            throw new PreparedStatementInvalid("wrong number of bind variables (" + bindCount + " for " + valuesCount + ") in: " + statement);
        }
    };

    private static String replaceBindVariable(Object value) {
        if (value instanceof Relation) {
            return ((Relation) value).except(CONSTRUCTOR).toJpql();
        } else {
            return literalBoundValue(value);
        }
    }

    private static String literalBoundValue(Object value) {
        if (value instanceof List) {
            Supplier<Stream<String>> literalized = () -> ((List) value).stream().map(v -> literal(v));

            if (literalized.get().findFirst().isEmpty()) {
                return literal(null);
            } else {
                return literalized.get().collect(joining(","));
            }
        } else {
            return literal(value);
        }
    }

}