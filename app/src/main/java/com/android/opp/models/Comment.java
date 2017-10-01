package com.android.opp.models;

import java.io.Serializable;

/**
 * Created by camilo on 1/7/17.
 */

public class Comment  implements Serializable {
    private int post_id;
    private int id;
    private String comment;
    private User user;
    private String create_at;

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public Comment(int post_id, int id, String comment, User user, String create_at) {
        this.post_id = post_id;
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.create_at = create_at;
    }




    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment(int post_id, int id, String comment, User user) {
        this.post_id = post_id;
        this.id = id;
        this.comment = comment;
        this.user = user;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment(int post_id, int id, String comment) {
        this.post_id = post_id;
        this.id = id;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "post_id=" + post_id +
                ", id=" + id +
                ", comment='" + comment + '\'' +
                '}';
    }
}
