package com.activepersistence.repository.models;

import com.activepersistence.model.BaseIdentity;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends BaseIdentity {

    @Lob
    private String body;

    @ManyToOne
    private Post post;

    public Comment() {
    }

    public Comment(String body, Post post) {
        this.body = body; this.post = post;
    }

    //<editor-fold defaultstate="collapsed" desc="GET/SET">
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    //</editor-fold>

}
