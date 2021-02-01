package com.activepersistence.repository.arel.nodes;

import com.activepersistence.repository.arel.Source;

public class StringJoin extends Join implements Source {

    public StringJoin(JpqlLiteral path) {
        super(path);
    }

}
