package com.example.tomas.becomebasketballpro.Model;

import java.io.Serializable;

/**
 * Created by Tomas on 05/09/2016.
 */
public class MotivationModel implements Serializable {

    private String thumbnail;
    private String articleImage;
    private int id;

    public MotivationModel() {
    }

//    public MotivationModel(String articleTitle) {
//        //this.articleTitle = articleTitle;
//        // this.thumbnail = thumbnail;
//        this.articleData = articleData;
//        this.articleImage = articleImage;
//        this.articleBody = articleBody;
//    }
    public MotivationModel(int id, String thumbnail, String articleImage) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.articleImage = articleImage;
    }

    public MotivationModel(int id) {
        this.id = id;
        //this.articleTitle = articleTitle;
        this.thumbnail = thumbnail;
        //this.articleData = articleData;
        this.articleImage = articleImage;
        //this.articleBody = articleBody;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

//    public String getTitle() {
//        return articleTitle;
//    }
//
//    public void setTitle(String articleTitle) {
//        this.articleTitle = articleTitle;
//    }

//    public String getData() {
//        return articleData;
//    }
//
//    public void setData(String articleData) {
//        this.articleData = articleData;
//    }

    public String getImage() {
        return articleImage;
    }

    public void setImage(String articleImage) {
        this.articleImage = articleImage;
    }

//    public String getBody() {
//        return articleBody;
//    }
//
//    public void setBody(String articleBody) {
//        this.articleBody = articleBody;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
