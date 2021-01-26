package com.activepersistence.repository.arel.nodes;

import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.LockModeType;

public class SelectStatement extends Node {

    private final SelectCore core;

    private final List<Visitable> orders = new ArrayList();

    private HashMap<String, Object> hints = new HashMap();

    private LockModeType lock = LockModeType.NONE;

    private int limit;

    private int offset;

    public SelectStatement(Entity entity) {
        this.core = new SelectCore(entity);
    }

    public SelectCore getCore() {
        return core;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public LockModeType getLock() {
        return lock;
    }

    public void setLock(LockModeType lock) {
        this.lock = lock;
    }

    public void setHints(HashMap<String, Object> hints) {
        this.hints = hints;
    }

    public HashMap<String, Object> getHints() {
        return hints;
    }

    public List<Visitable> getOrders() {
        return orders;
    }

}
