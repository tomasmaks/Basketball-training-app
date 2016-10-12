package com.example.tomas.becomebasketballpro.Model;

import java.io.Serializable;

/**
 * Created by Tomas on 05/09/2016.
 */
public class SuccessModel implements Serializable {

    private String thumb;
    private String title;
    private String published_date;
    private String photo;
    private String body;
    private String id;
    private String video;

    public SuccessModel() {
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }


}
