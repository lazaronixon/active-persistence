package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;

public class StringJoin extends Join implements Source {

    public StringJoin(JpqlLiteral path) {
        super(path);
    }

}
