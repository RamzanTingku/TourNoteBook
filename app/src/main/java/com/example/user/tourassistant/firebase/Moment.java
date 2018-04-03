package com.example.user.tourassistant.firebase;

/**
 * Created by Ramzan Ullah on 10/6/2017.
 */

public class Moment {


    private String image;
    private String title;
    private String desc;
    private String userName;

    public Moment() {
    }

    public Moment(String image, String title, String desc, String userName) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
