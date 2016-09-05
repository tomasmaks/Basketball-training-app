package com.example.tomas.becomebasketballpro.Listeners;

import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.Model.MotivationModel;

import java.util.List;

/**
 * Created by Tomas on 05/09/2016.
 */
public interface MotivationListener {

    public void addMotivation(MotivationModel motivation);

    public List<MotivationModel> getAllMotivation();

    public int getMotivationCount();


}
