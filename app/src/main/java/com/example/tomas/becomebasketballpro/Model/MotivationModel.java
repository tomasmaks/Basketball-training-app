package com.example.tomas.becomebasketballpro.Model;

import java.io.Serializable;

/**
 * Created by Tomas on 05/09/2016.
 */
public class MotivationModel implements Serializable {

    private String thumbnail;
    private String articleTitle;
    private String articleData;
    private String articleImage;
    private String articleBody;
    private int id;

    public MotivationModel() {
    }

    public MotivationModel(String articleTitle) {
        this.articleTitle = articleTitle;
        // this.thumbnail = thumbnail;
        this.articleData = articleData;
        this.articleImage = articleImage;
        this.articleBody = articleBody;
    }
    public MotivationModel(int id, String articleTitle) {
        this.id = id;
        this.articleTitle = articleTitle;
    }

    public MotivationModel(int id, String articleTitle, String thumbnail, String articleData, String articleImage, String articleBody) {
        this.id = id;
        this.articleTitle = articleTitle;
        this.thumbnail = thumbnail;
        this.articleData = articleData;
        this.articleImage = articleImage;
        this.articleBody = articleBody;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return articleTitle;
    }

    public void setTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getData() {
        return articleData;
    }

    public void setData(String articleData) {
        this.articleData = articleData;
    }

    public String getImage() {
        return articleImage;
    }

    public void setImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getBody() {
        return articleBody;
    }

    public void setBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
