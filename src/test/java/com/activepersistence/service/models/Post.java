package com.activepersistence.service.models;

import com.activepersistence.model.Base;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class Post extends Base<Integer> {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String title;

    @Lob
    private String body;

    private Integer likesCount;

    @OneToMany(mappedBy = "post", cascade = {PERSIST, REMOVE})
    private List<Comment> comments = new ArrayList();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Post() {
    }

    public Post(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Post(String title, String body, Integer likesCount) {
        this.title = title;
        this.body = body;
        this.likesCount = likesCount;
    }

    public Comment commentsBuild(String body) {
        var comment = new Comment(body, this);
        comments.add(comment);
        return comment;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
