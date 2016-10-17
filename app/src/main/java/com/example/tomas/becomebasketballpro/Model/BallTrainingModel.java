package com.example.tomas.becomebasketballpro.Model;

import java.io.Serializable;


/**
 * Created by Tomas on 12/08/2016.
 */
public class BallTrainingModel implements Serializable {
    private String catThumb;
    private String category;
    private int ids;
    private String name;
    private String thumb;
    private String description;
    private String video;
    private String body;
    private int id;

    public BallTrainingModel() {
    }

    public String getCatThumb() {
        return catThumb;
    }

    public void setCatThumb(String catThumb) {
        this.catThumb = catThumb;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
