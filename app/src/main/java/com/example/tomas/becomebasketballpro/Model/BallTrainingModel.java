package com.example.tomas.becomebasketballpro.Model;

import android.support.v7.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Tomas on 12/08/2016.
 */
public class BallTrainingModel {
    private String thumbnail;
    private String category;
    @SerializedName("cast")
    private List<Cast> castList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Cast> getCastList() {
        return castList;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public static class Cast {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
