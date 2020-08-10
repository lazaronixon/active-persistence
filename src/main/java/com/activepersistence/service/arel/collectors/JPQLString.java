package com.activepersistence.service.arel.collectors;

import java.util.function.Function;


public class JPQLString extends PlainString<JPQLString> {

    private int bindIndex;

    public JPQLString() {
        this.bindIndex = 1;
    }

    public JPQLString addBind(Object bind, Function<Integer, String> yield) {
        this.append(yield.apply(bindIndex));
        this.bindIndex = bindIndex + 1;
        return this;
    }

}
