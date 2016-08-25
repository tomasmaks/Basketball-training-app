package com.example.tomas.becomebasketballpro.Model;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Tomas on 12/08/2016.
 */
public class BallTrainingModel {
    private String category;
    private String ids;
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
   }

        private String name;
        private String thumb;
        private String description;
        private String video;
        private String body;
        private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoURI() {
            return video;
        }

        public void setVideoURI(String video) {
            this.video = video;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

}
