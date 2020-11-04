package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;

public class PostsService extends Base<Post> {

    public Relation<Post> oneEqZero() {
        return where("1=0");
    }

    public Relation<Post> twoEqZero() {
        return where("2=0");
    }

}
