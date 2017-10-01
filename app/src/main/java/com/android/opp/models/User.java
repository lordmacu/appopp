package com.android.opp.models;

import com.android.opp.helpers.HttpHelper;

import java.io.Serializable;

/**
 * Created by camilo on 1/7/17.
 */

public class User  implements Serializable{
    private  String id;
    private String name;
    private String image;
    private String user_id;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", user_id='" + user_id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public User() {
        setType("1");
    }

    public User(String id) {
        this.id = id;
        setType("1");

    }

    public User(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
        setType("1");

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {

        String thumbail="";
        HttpHelper helper= new HttpHelper();
        if(getType().equals("2")){
            thumbail =image;
        }else{
            thumbail= helper.getUserUrlThumbail()+image;

        }

        return thumbail;
    }



    public void setImage(String image) {
        this.image = image;
    }
}
