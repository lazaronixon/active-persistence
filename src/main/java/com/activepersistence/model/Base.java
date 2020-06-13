package com.activepersistence.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class Base<ID> {

    public abstract ID getId();

    public boolean isNewRecord() {
        return getId() == null;
    }

    public boolean isPersisted() {
        return getId() != null;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        // If you want timestamp it should be overridden on child.
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        // If you want timestamp it should be overridden on child.
    }

    @PrePersist
    private void beforeCreate() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    private void beforeUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public int hashCode() {
        return (getId() != null) ? Objects.hash(getId()) : super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return (getId() != null && getClass().isInstance(other) && other.getClass().isInstance(this))
                ? getId().equals(((Base<?>) other).getId())
                : (other == this);
    }

}
