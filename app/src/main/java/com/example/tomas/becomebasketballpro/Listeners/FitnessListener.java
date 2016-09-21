package com.example.tomas.becomebasketballpro.Listeners;

import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;

import java.util.List;

/**
 * Created by Tomas on 07/09/2016.
 */
public interface FitnessListener {
    public void addCategory(FitnessTrainingModel fitnessTrainingModel);

    public List<FitnessTrainingModel> getAllCategories();

}
