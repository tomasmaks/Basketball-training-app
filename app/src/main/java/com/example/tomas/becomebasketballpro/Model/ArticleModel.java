package com.example.tomas.becomebasketballpro.Model;

/**
 * Created by Tomas on 03/08/2016.
 */
public class ArticleModel {
    private String thumbnail;
    private String articleTitle;
    private String articleSubtitle;
    private String articleAuthor;


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

    public String getSubtitle() {
        return articleSubtitle;
    }

    public void setSubtitle(String articleSubtitle) {
        this.articleSubtitle = articleSubtitle;
    }

    public String getAuthor() {
        return articleAuthor;
    }

    public void setAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }
}
