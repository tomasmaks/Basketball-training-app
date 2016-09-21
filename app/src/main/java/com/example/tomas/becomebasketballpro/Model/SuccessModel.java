package com.example.tomas.becomebasketballpro.Model;

import java.io.Serializable;

/**
 * Created by Tomas on 05/09/2016.
 */
public class SuccessModel implements Serializable {

    private String thumbnail;
    private String articleTitle;
    private String articleData;
    private String articleImage;
    private String articleBody;
    private int id;
    private String video;

    public SuccessModel() {
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

    public String getVideoURI() {
        return video;
    }

    public void setVideoURI(String video) {
        this.video = video;
    }


}
