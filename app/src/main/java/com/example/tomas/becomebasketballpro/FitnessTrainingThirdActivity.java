package com.example.tomas.becomebasketballpro;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 05/09/2016.
 */
public class FitnessTrainingThirdActivity extends Activity {

    String exercise_name, exercise_body;
    String exercise_video;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    boolean showAudioUi;
    boolean showFadeAnim;
    private boolean advertised = false;
    ImageButton play;
    ImageView thumbnail;

    public static final String EXTRA_POST_KEY = "post_key";
    public static final String EXTRA_DETAIL_KEY = "detail_key";

    private InterstitialAd mInterstitialAd;

    int mPassPostKey;
    int mPostKey;
    int mPassDetailKey;
    int mDetailKey;
    DatabaseReference mReference;
    TextView article_title;
    TextView article_body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fitnesstraining_details);

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        mPostKey = getIntent().getIntExtra(EXTRA_POST_KEY, mPassPostKey);

        mDetailKey = getIntent().getIntExtra(EXTRA_DETAIL_KEY, mPassDetailKey);

        setupVideoView();

        // Initialize Database
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://basketball-training-app.firebaseio.com/fitness/").child(String.valueOf(mPostKey)).child("exercises").child(String.valueOf(mDetailKey));

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();

                article_title.setText(name);

                String body = (String) dataSnapshot.child("body").getValue();

                if (body.isEmpty()) {
                    article_body.setVisibility(View.GONE);
                } else {
                    article_body.setText(Html.fromHtml(body).toString());
                }

                String video = (String) dataSnapshot.child("video").getValue();

                exercise_video = video;

                Picasso.with(getBaseContext())
                        .load(YouTubeThumbnail.getUrlFromVideoId(exercise_video, Quality.HIGH))
                        .fit()
                        .centerCrop()
                        .into(thumbnail);

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FitnessTrainingThirdActivity.this, YouTubePlayerActivity.class);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, exercise_video);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, playerStyle);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, orientation);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, showAudioUi);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, 1);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FitnessTrainingThirdActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                FirebaseCrash.log(databaseError.toString());
            }
        });
    }

    private void setupVideoView() {

        playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        orientation = Orientation.AUTO;
        showAudioUi = true;
        showFadeAnim = true;

        play = (ImageButton) findViewById(R.id.play_bt);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        article_title = (TextView) findViewById(R.id.exercise_title);
        article_title.setText(exercise_name);
        article_body = (TextView) findViewById(R.id.exercise_body);
        article_body.setText(exercise_body);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}