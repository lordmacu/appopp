package com.android.opp.models;

import com.android.opp.helpers.HttpHelper;

import java.io.Serializable;

/**
 * Created by camilo on 3/6/17.
 */

public class ImageItem  implements Serializable {

    private int type;
    private int id;
    private String width;
    private String height;
    private String ThumbWidth;
    private String ThumbHeight;

    public ImageItem() {
     }

    @Override
    public String toString() {
        return "ImageItem{" +
                "type=" + type +
                ", id=" + id +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", ThumbWidth='" + ThumbWidth + '\'' +
                ", ThumbHeight='" + ThumbHeight + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public ImageItem(int type, int id, String width, String height, String url, String thumbWidth, String thumbHeight) {
        this.type = type;
        this.id = id;
        this.width = width;
        this.height = height;
        ThumbWidth = thumbWidth;
        ThumbHeight = thumbHeight;
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getThumbWidth() {
        return ThumbWidth;
    }

    public void setThumbWidth(String thumbWidth) {
        ThumbWidth = thumbWidth;
    }

    public String getThumbHeight() {
        return ThumbHeight;
    }

    public void setThumbHeight(String thumbHeight) {
        ThumbHeight = thumbHeight;
    }

    public ImageItem(int type, int id, String url) {
        this.type = type;
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private  String url;

    public ImageItem(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {

        return url;
    }

    public String getUrlThumbail() {

        HttpHelper helper= new HttpHelper();
        return helper.getUrlThumbail()+url;
    }

    public String getUrlFull() {

        HttpHelper helper= new HttpHelper();
        return helper.getAssetUrl()+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageItem(String url) {
        this.url = url;
    }
}
