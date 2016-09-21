package com.example.tomas.becomebasketballpro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tomas.becomebasketballpro.DBHandler.ArticleDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.ui.ToastAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.enums.Quality;
import com.thefinestartist.ytpa.utils.YouTubeThumbnail;

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
    private RelativeLayout video;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    private static String VIDEO_ID = "iS1g8G_njx8";
    boolean showAudioUi;
    boolean showFadeAnim;
    private boolean advertised = false;
    ImageButton play;
    ImageView thumbnail;

    String exercise_video;

    private InterstitialAd mInterstitialAd;

    // private ProgressBar progressBar;
    List<ArticleModel> articleModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);


        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


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
        if(bundle != null) {

            String json = bundle.getString("articleModel");
  //          if (utils.isConnectingToInternet()) {

                ArticleModel articleModel = new Gson().fromJson(json, ArticleModel.class);
                ImageLoader.getInstance().displayImage(articleModel.getImage(), article_image);

                exercise_video = articleModel.getVideoURI();

                Picasso.with(this)
                        .load(YouTubeThumbnail.getUrlFromVideoId(exercise_video, Quality.HIGH))
                        .fit()
                        .centerCrop()
                        .into(thumbnail);


                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ArticleDetailsActivity.this, YouTubePlayerActivity.class);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, exercise_video);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, playerStyle);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, orientation);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, showAudioUi);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, 1);
                    }
                });

                article_title.setText(articleModel.getTitle());
                article_body.setText(Html.fromHtml(articleModel.getBody()));
                article_data.setText("Added on: " + articleModel.getData());

                if (articleModel.getImage().isEmpty()) {
                    article_image.setVisibility(View.GONE);
                }

                if (exercise_video.isEmpty()) {
                    play.setVisibility(View.GONE);
                    thumbnail.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                }

        }
    }


    private void setUpUIViews() {
        article_image = (ImageView)findViewById(R.id.article_image);
        article_title = (TextView)findViewById(R.id.article_title);
        article_body = (TextView)findViewById(R.id.article_body);
        article_data = (TextView)findViewById(R.id.article_data);
        video = (RelativeLayout)findViewById(R.id.video);

        playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        orientation = Orientation.AUTO;
        showAudioUi = true;
        showFadeAnim = true;

        play = (ImageButton) findViewById(R.id.play_bt);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
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

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}
