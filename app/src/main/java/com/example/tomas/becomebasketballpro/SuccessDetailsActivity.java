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

import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.enums.Quality;
import com.thefinestartist.ytpa.utils.YouTubeThumbnail;

/**
 * Created by Tomas on 09/08/2016.
 */
public class SuccessDetailsActivity extends ActionBarActivity {


    private ImageView articleImage;
    private TextView articleTitle;
    private TextView articleBody;
    private TextView articleData;
    private RelativeLayout rel_video;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    boolean showAudioUi;
    boolean showFadeAnim;
    ImageButton play;
    ImageView thumbnail;

    String exerciseVideo;
    private InterstitialAd interstitialAd;

    String postKey;
    DatabaseReference reference;

    public static final String EXTRA_SUCCESS_KEY = "success_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_detail);

        interstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        postKey = getIntent().getStringExtra(EXTRA_SUCCESS_KEY);

        // Initialize Database
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://basketball-training-app.firebaseio.com/success/");

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // setting up text views and stuff

        setUpUIViews();

        reference.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();

                articleTitle.setText(title);

                String body = (String) dataSnapshot.child("body").getValue();

                if (body.isEmpty()) {
                    articleBody.setVisibility(View.GONE);
                } else {
                    articleBody.setText(Html.fromHtml(body).toString());
                }

                String data = (String) dataSnapshot.child("published_date").getValue();

                articleData.setText("Added on: " + data);

                String video = (String) dataSnapshot.child("video").getValue();

                exerciseVideo = video;

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
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, exerciseVideo);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, playerStyle);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, orientation);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, showAudioUi);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, 1);
                    }
                });

                if (image.isEmpty()) {
                    articleImage.setVisibility(View.GONE);
                } else {
                    Picasso.with(getBaseContext()).load(image).into(articleImage);
                }

                if (exerciseVideo.isEmpty()) {
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
        articleImage = (ImageView) findViewById(R.id.image_article);
        articleTitle = (TextView) findViewById(R.id.text_title);
        articleBody = (TextView) findViewById(R.id.text_body);
        articleData = (TextView) findViewById(R.id.text_data);

        rel_video = (RelativeLayout) findViewById(R.id.rel_video);

        playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        orientation = Orientation.AUTO;
        showAudioUi = true;
        showFadeAnim = true;

        play = (ImageButton) findViewById(R.id.image_play);
        thumbnail = (ImageView) findViewById(R.id.image_thumbnail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
}
