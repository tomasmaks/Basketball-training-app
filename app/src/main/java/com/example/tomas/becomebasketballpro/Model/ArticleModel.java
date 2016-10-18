package com.example.tomas.becomebasketballpro.Model;

import com.google.firebase.database.PropertyName;

/**
 * Created by Tomas on 03/08/2016.
 */
public class ArticleModel {

    @PropertyName("thumb")
    private String thumb;

    @PropertyName("title")
    private String title;

    @PropertyName("published_date")
    private String published_date;

    @PropertyName("photo")
    private String photo;

    @PropertyName("body")
    private String body;

    @PropertyName("id")
    private String id;

    @PropertyName("video")
    private String video;

    public ArticleModel() {
    }

    public ArticleModel(String id, String title, String thumb, String published_date, String photo, String body, String video) {
        this.id = id;
        this.title = title;
        this.thumb = thumb;
        this.published_date = published_date;
        this.photo = photo;
        this.body = body;
        this.video = video;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideoURI() {
        return video;
    }

    public void setVideoURI(String video) {
        this.video = video;
    }


}
