package com.android.opp.models;

import java.io.Serializable;

/**
 * Created by camilo on 1/7/17.
 */

public class Interest   implements Serializable {
    private String id;
    private String name;
    private String id_interest;

    public String getId_interest() {
        return id_interest;
    }

    public void setId_interest(String id_interest) {
        this.id_interest = id_interest;
    }

    public Interest() {



    }

    @Override
    public String toString() {
        return "Interest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", id_interest='" + id_interest + '\'' +
                '}';
    }

    public Interest(String id, String name) {
        this.id = id;
        this.name = name;
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

}


