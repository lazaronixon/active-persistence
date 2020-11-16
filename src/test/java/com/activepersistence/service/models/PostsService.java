package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import com.activepersistence.service.callbacks.AfterCreate;
import com.activepersistence.service.callbacks.AfterDestroy;
import com.activepersistence.service.callbacks.AfterUpdate;
import com.activepersistence.service.callbacks.BeforeCreate;
import com.activepersistence.service.callbacks.BeforeDestroy;
import com.activepersistence.service.callbacks.BeforeUpdate;
import javax.enterprise.event.Observes;

public class PostsService extends Base<Post> {

    public Relation<Post> oneEqZero() {
        return where("1=0");
    }

    public Relation<Post> twoEqZero() {
        return where("2=0");
    }

    private void beforeCreate(@Observes @BeforeCreate Post post) {
        if (post.getBody().equals("execBeforeCreate")) post.setBody("OK");
    }

    private void afterCreate(@Observes @AfterCreate Post post) {
        if (post.getBody().equals("execAfterCreate")) post.setBody("OK");
    }

    private void beforeUpdate(@Observes @BeforeUpdate Post post) {
        if (post.getBody().equals("execBeforeUpdate")) post.setBody("OK");
    }

    private void afterUpdate(@Observes @AfterUpdate Post post) {
        if (post.getBody().equals("execAfterUpdate")) post.setBody("OK");
    }

    private void beforeDestroy(@Observes @BeforeDestroy Post post) {
        if (post.getBody().equals("execBeforeDestroy")) post.setBody("OK");
    }

    private void afterDestroy(@Observes @AfterDestroy Post post) {
        if (post.getBody().equals("execAfterDestroy")) post.setBody("OK");
    }

}
