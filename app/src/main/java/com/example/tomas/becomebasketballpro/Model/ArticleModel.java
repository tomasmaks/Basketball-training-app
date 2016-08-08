package com.example.tomas.becomebasketballpro.Model;

/**
 * Created by Tomas on 03/08/2016.
 */
public class ArticleModel {
    private String thumbnail;
    private String articleTitle;
    private String articleData;
    private String articleImage;
    private String articleBody;


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


}
