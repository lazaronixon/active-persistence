package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;

public class PostsService extends Base<Post, Long> {

    public PostsService() {
        super(Post.class);
    }

    public Relation<Post, Long> oneEqZero() {
        return where("1=0");
    }

    public Relation<Post, Long> twoEqZero() {
        return where("2=0");
    }

}
