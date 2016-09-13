package com.example.tomas.becomebasketballpro.Listeners;

import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;

import java.util.List;

/**
 * Created by Tomas on 13/09/2016.
 */
public interface BallTrainingListener {

    public void addCategory(BallTrainingModel ballTrainingModel);
    
    public List<BallTrainingModel> getAllCategories();


}
