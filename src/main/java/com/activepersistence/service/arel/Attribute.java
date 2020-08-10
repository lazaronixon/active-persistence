package com.activepersistence.service.arel;

import com.activepersistence.service.arel.nodes.Equality;
import com.activepersistence.service.arel.nodes.Literalized;
import com.activepersistence.service.arel.nodes.NotEqual;
import com.activepersistence.service.arel.visitors.Visitable;

public class Attribute implements Visitable {

    private final Entity relation;

    private final String name;

    public Attribute(Entity relation, String name) {
        this.relation = relation;
        this.name = name;
    }

    public Entity getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }

    public Equality eq(Object other) {
        return new Equality(this, literalized(other));
    }

    public NotEqual notEq(Object other) {
        return new NotEqual(this, literalized(other));
    }

    private Visitable literalized(Object other) {
        if (other == null) {
            return null;
        } else if (other instanceof Visitable) {
            return (Visitable) other;
        } else {
            return new Literalized(other);
        }
    }

}
