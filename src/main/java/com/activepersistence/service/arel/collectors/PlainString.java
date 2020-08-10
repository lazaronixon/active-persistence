package com.activepersistence.service.arel.collectors;

public class PlainString<T> {

    private final StringBuilder str;

    public PlainString() {
        this.str = new StringBuilder();
    }

    public String getValue() {
        return str.toString();
    }

    public T append(String value) {
        str.append(value); return (T) this;
    }

    public T append(Object value) {
        str.append(value); return (T) this;
    }
}
