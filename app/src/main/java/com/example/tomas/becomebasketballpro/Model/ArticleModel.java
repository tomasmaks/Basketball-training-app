package com.example.tomas.becomebasketballpro.Model;

import java.io.Serializable;

/**
 * Created by Tomas on 03/08/2016.
 */
public class ArticleModel implements Serializable {
    private String thumbnail;
    private String articleTitle;
    private String articleData;
    private String articleImage;
    private String articleBody;
    private int id;
    private String video;

    public ArticleModel() {
    }

    public ArticleModel(String articleTitle) {
        this.articleTitle = articleTitle;
        // this.thumbnail = thumbnail;
        this.articleData = articleData;
        this.articleImage = articleImage;
        this.articleBody = articleBody;
    }
    public ArticleModel(int id, String articleTitle) {
        this.id = id;
        this.articleTitle = articleTitle;
    }

    public ArticleModel(int id, String articleTitle, String thumbnail, String articleData, String articleImage, String articleBody, String video) {
        this.id = id;
        this.articleTitle = articleTitle;
        this.thumbnail = thumbnail;
        this.articleData = articleData;
        this.articleImage = articleImage;
        this.articleBody = articleBody;
        this.video = video;
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
