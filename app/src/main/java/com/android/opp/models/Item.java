package com.android.opp.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by camilo on 30/5/17.
 */

public class Item  implements Serializable {

    private String id;
    private String title;
    private String url;
    private String likes;
    private String haceTanto;
    private String userImage;
    private int grilla;
    
    private ArrayList<ImageItem> images;
    private ArrayList<Interest> interests;
    private ArrayList<Comment> comments;
    private String description;
    private String edad_desde;
    private String edad_hasta;
    private String genero;
    private String alcance;
    private String latitude;
    private String longitude;
    private String city;
    private String country;
    private User user;
    private String created_at;
    private int count_likes;
    private int like_user;
    private int itemvotem=0;
    private boolean isVoting=false;

    private String cantidadVotos="0";

    public String getCantidadVotos() {
        return cantidadVotos;
    }

    public void setCantidadVotos(String cantidadVotos) {
        this.cantidadVotos = cantidadVotos;
    }

    public int getItemvotem() {
        return itemvotem;
    }

    public void setItemvotem(int itemvotem) {
        this.itemvotem = itemvotem;
    }

    public boolean isVoting(int itemvote) {

        boolean retorno=false;
        if(itemvotem==itemvote){
            retorno=true;
        }else{
            retorno= false;
        }


        return retorno;
    }

    public void setVoting(boolean voting,int itemvote) {
        isVoting = voting;
        itemvotem=itemvote;
    }

    public int getCount_likes() {
        return count_likes;
    }

    public void setCount_likes(int count_likes) {
        this.count_likes = count_likes;
    }

    public int getLike_user() {
        return like_user;
    }

    public void setLike_user(int like_user) {
        this.like_user = like_user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Interest> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<Interest> interests) {
        this.interests = interests;
    }

    public String getGenero() {
        return genero;
    }

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageItem> images) {
        this.images = images;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEdad_desde() {
        return edad_desde;
    }

    public void setEdad_desde(String edad_desde) {
        this.edad_desde = edad_desde;
    }

    public String getEdad_hasta() {
        return edad_hasta;
    }

    public void setEdad_hasta(String edad_hasta) {
        this.edad_hasta = edad_hasta;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGrilla() {
        return grilla;
    }

    public void setGrilla(int grilla) {
        this.grilla = grilla;
    }


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getHaceTanto() {
        return haceTanto;
    }

    public void setHaceTanto(String haceTanto) {
        this.haceTanto = haceTanto;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }


    public Item() {

    }


    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", likes='" + likes + '\'' +
                ", haceTanto='" + haceTanto + '\'' +
                ", userImage='" + userImage + '\'' +
                ", grilla=" + grilla +
                ", images=" + images +
                ", interests=" + interests +
                ", comments=" + comments +
                ", description='" + description + '\'' +
                ", edad_desde='" + edad_desde + '\'' +
                ", edad_hasta='" + edad_hasta + '\'' +
                ", genero='" + genero + '\'' +
                ", alcance='" + alcance + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", user=" + user +
                '}';
    }
}
