package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;

public class PostService extends Base<Post> {

    public Relation<Post> oneEqZero() {
        return where("1=0");
    }

    public Relation<Post> twoEqZero() {
        return where("2=0");
    }

    @Override
    public void beforeSave(Post post) {
        if (post.getBody().equals("execBeforeSave")) post.setBody("OK");
    }

    @Override
    public void afterSave(Post post) {
        if (post.getBody().equals("execAfterSave")) post.setBody("OK");
    }

    @Override
    public void beforeCreate(Post post) {
        if (post.getBody().equals("execBeforeCreate")) post.setBody("OK");
    }

    @Override
    public void afterCreate(Post post) {
        if (post.getBody().equals("execAfterCreate")) post.setBody("OK");
    }

    @Override
    public void beforeUpdate(Post post) {
        if (post.getBody().equals("execBeforeUpdate")) post.setBody("OK");
    }

    @Override
    public void afterUpdate(Post post) {
        if (post.getBody().equals("execAfterUpdate")) post.setBody("OK");
    }

    @Override
    public void beforeDestroy(Post post) {
        if (post.getBody().equals("execBeforeDestroy")) post.setBody("OK");
    }

    @Override
    public void afterDestroy(Post post) {
        if (post.getBody().equals("execAfterDestroy")) post.setBody("OK");
    }

}
