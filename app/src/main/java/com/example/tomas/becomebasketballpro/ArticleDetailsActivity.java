package com.example.tomas.becomebasketballpro;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tomas.becomebasketballpro.DBHandler.ArticleDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 06/08/2016.
 */
public class ArticleDetailsActivity extends ActionBarActivity {

    private ImageView article_image;
    private TextView article_title;
    private TextView article_body;
    private TextView article_data;

   String articleTitle;
    // private ProgressBar progressBar;
    List<ArticleModel> articleModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        // Showing and Enabling clicks on the Home/Up button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // setting up text views and stuff
        setUpUIViews();


        ArticleDbHandler dbHandler = new ArticleDbHandler(ArticleDetailsActivity.this);
        NetworkUtils utils = new NetworkUtils(ArticleDetailsActivity.this);

        // recovering data from MainActivity, sent via intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            String json = bundle.getString("articleModel");
            if(utils.isConnectingToInternet()) {

                ArticleModel articleModel = new Gson().fromJson(json, ArticleModel.class);
                ImageLoader.getInstance().displayImage(articleModel.getImage(), article_image);

                article_title.setText(articleModel.getTitle());
                article_body.setText(articleModel.getBody());
                article_data.setText("Added on: " + articleModel.getData());

            }else{

                ArticleModel articleModel = new Gson().fromJson(json, ArticleModel.class);

                ImageLoader.getInstance().displayImage(articleModel.getImage(), article_image);

                article_title.setText(articleModel.getTitle());
                article_body.setText(articleModel.getBody());
                article_data.setText("Added on: " + articleModel.getData());


            }
            // Then later, when you want to display image



        }

    }

    private void setUpUIViews() {
        article_image = (ImageView)findViewById(R.id.article_image);
        article_title = (TextView)findViewById(R.id.article_title);
        article_body = (TextView)findViewById(R.id.article_body);
        article_data = (TextView)findViewById(R.id.article_data);
        // progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
