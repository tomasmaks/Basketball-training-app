package com.example.tomas.becomebasketballpro.Listeners;

import com.example.tomas.becomebasketballpro.Model.SuccessModel;

import java.util.List;

/**
 * Created by Tomas on 05/09/2016.
 */
public interface SuccessListener {
    public void addSuccess(SuccessModel success);

    public List<SuccessModel> getAllSuccess();

    public int getSuccessCount();
}
