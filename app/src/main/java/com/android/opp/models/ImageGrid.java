package com.android.opp.models;

/**
 * Created by camilo on 10/6/17.
 */

public class ImageGrid {
    private int id;
    private String title;
    private int resource;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ImageGrid(int id, String title, int resource, int state) {
        this.id = id;
        this.title = title;
        this.resource = resource;
        this.state = state;
    }

    public ImageGrid(int id, String title, int resource) {
        this.id = id;
        this.title = title;
        this.resource = resource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }


    @Override
    public String toString() {
        return "ImageGrid{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", resource=" + resource +
                '}';
    }
}
