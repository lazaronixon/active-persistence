package com.activepersistence.model;

import com.activepersistence.ReadOnlyRecord;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class Base<ID> {

    @Transient private boolean newRecord = true;

    @Transient private boolean destroyed = false;

    @Transient private boolean readOnly  = false;

    public abstract ID getId();

    public abstract void setId(ID value);

    public boolean isNewRecord() {
        return newRecord;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isPersisted() {
        return !(newRecord || destroyed);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        // If you want timestamp it should be overridden on child.
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        // If you want timestamp it should be overridden on child.
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;

        return Objects.equals(getId(), ((Base) other).getId());
    }

    @PrePersist
    private void prePersist() {
        if (readOnly) {
            raiseReadOnlyRecordError();
        } else {
            setCreatedAt(LocalDateTime.now());
            setUpdatedAt(LocalDateTime.now());
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (readOnly) {
            raiseReadOnlyRecordError();
        } else {
            setUpdatedAt(LocalDateTime.now());
        }
    }

    @PreRemove
    private void preRemove() {
        if (readOnly) raiseReadOnlyRecordError();
    }

    @PostPersist
    private void postPersist() {
        newRecord = false;
    }

    @PostUpdate
    private void postUpdate() {
        newRecord = false;
    }

    @PostRemove
    private void postRemove() {
        destroyed = true;
    }

    @PostLoad
    private void postLoad() {
        newRecord = false;
    }

    private void raiseReadOnlyRecordError() {
      throw new ReadOnlyRecord(getClass().getSimpleName() + " is marked as readonly");
    }

}
