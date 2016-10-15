package com.example.tomas.becomebasketballpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.Model.SuccessModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.enums.Quality;
import com.thefinestartist.ytpa.utils.YouTubeThumbnail;

/**
 * Created by Tomas on 09/08/2016.
 */
public class SuccessDetailsActivity extends ActionBarActivity {


    private ImageView article_image;
    private TextView article_title;
    private TextView article_body;
    private TextView article_data;
    private RelativeLayout rel_video;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    boolean showAudioUi;
    boolean showFadeAnim;
    ImageButton play;
    ImageView thumbnail;;
    String exercise_video;
    private InterstitialAd mInterstitialAd;

    String mPostKey;
    DatabaseReference mReference;

    public static final String EXTRA_SUCCESS_KEY = "success_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_detail);

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

        mPostKey = getIntent().getStringExtra(EXTRA_SUCCESS_KEY);

        // Initialize Database
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://basketball-training-app.firebaseio.com/success/");

        // Showing and Enabling clicks on the Home/Up button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // setting up text views and stuff

        setUpUIViews();

        mReference.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();

                article_title.setText(title);

                String body = (String) dataSnapshot.child("body").getValue();

                if (body.isEmpty()) {
                    article_body.setVisibility(View.GONE);
                } else {
                    article_body.setText(Html.fromHtml(body).toString());
                }

                String data = (String) dataSnapshot.child("published_date").getValue();

                article_data.setText("Added on: " + data);

                String video = (String) dataSnapshot.child("video").getValue();

                exercise_video = video;

                String image = (String) dataSnapshot.child("photo").getValue();

                Picasso.with(getBaseContext())
                        .load(YouTubeThumbnail.getUrlFromVideoId(video, Quality.HIGH))
                        .fit()
                        .centerCrop()
                        .into(thumbnail);

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SuccessDetailsActivity.this, YouTubePlayerActivity.class);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, exercise_video);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, playerStyle);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, orientation);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, showAudioUi);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, 1);
                    }
                });

                if (image.isEmpty()) {
                    article_image.setVisibility(View.GONE);
                } else {
                    Picasso.with(getBaseContext()).load(image).into(article_image);
                }

                if (exercise_video.isEmpty()) {
                    play.setVisibility(View.GONE);
                    thumbnail.setVisibility(View.GONE);
                    rel_video.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SuccessDetailsActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                FirebaseCrash.log(databaseError.toString());

            }
        });


    }

    private void setUpUIViews() {
        article_image = (ImageView)findViewById(R.id.article_image);
        article_title = (TextView)findViewById(R.id.article_title);
        article_body = (TextView)findViewById(R.id.article_body);
        article_data = (TextView)findViewById(R.id.article_data);

        rel_video = (RelativeLayout)findViewById(R.id.rel_video);

        playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        orientation = Orientation.AUTO;
        showAudioUi = true;
        showFadeAnim = true;

        play = (ImageButton) findViewById(R.id.play_bt);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
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
