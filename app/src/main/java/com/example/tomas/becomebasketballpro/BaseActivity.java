package com.example.tomas.becomebasketballpro;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Tomas on 13/10/2016.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}
