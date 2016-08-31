package com.example.tomas.becomebasketballpro.Listeners;

import com.example.tomas.becomebasketballpro.Model.ArticleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 30/08/2016.
 */
public interface ArticleListener {

    public void addArticle(ArticleModel article);

    public List<ArticleModel> getAllArticle();

    public int getArticleCount();
}
