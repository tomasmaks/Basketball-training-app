package com.example.tomas.becomebasketballpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
 * Created by Tomas on 05/09/2016.
 */
public class FitnessTrainingThirdActivity extends Activity {

    String exerciseName, exerciseBody, exerciseVideo;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    boolean showAudioUi;
    boolean showFadeAnim;
    ImageButton play;
    ImageView thumbnail;

    public static final String EXTRA_POST_KEY = "post_key";
    public static final String EXTRA_DETAIL_KEY = "detail_key";

    private InterstitialAd interstitialAd;

    int passPostKey;
    int postKey;
    int passDetailKey;
    int detailKey;
    DatabaseReference reference;
    TextView articleTitle;
    TextView articleBody;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fitnesstraining_details);

        interstitialAd = new InterstitialAd(this);

        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        postKey = getIntent().getIntExtra(EXTRA_POST_KEY, passPostKey);

        detailKey = getIntent().getIntExtra(EXTRA_DETAIL_KEY, passDetailKey);

        setupVideoView();

        // Initialize Database
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://basketball-training-app.firebaseio.com/fitness/").child(String.valueOf(postKey)).child("exercises").child(String.valueOf(detailKey));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();

                articleTitle.setText(name);

                String body = (String) dataSnapshot.child("body").getValue();

                if (body.isEmpty()) {
                    articleBody.setVisibility(View.GONE);
                } else {
                    articleBody.setText(Html.fromHtml(body).toString());
                }

                String video = (String) dataSnapshot.child("video").getValue();

                exerciseVideo = video;

                Picasso.with(getBaseContext())
                        .load(YouTubeThumbnail.getUrlFromVideoId(exerciseVideo, Quality.HIGH))
                        .fit()
                        .centerCrop()
                        .into(thumbnail);

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FitnessTrainingThirdActivity.this, YouTubePlayerActivity.class);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, exerciseVideo);
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

        play = (ImageButton) findViewById(R.id.image_play);
        thumbnail = (ImageView) findViewById(R.id.image_thumbnail);

        articleTitle = (TextView) findViewById(R.id.text_title);
        articleTitle.setText(exerciseName);
        articleBody = (TextView) findViewById(R.id.text_body);
        articleBody.setText(exerciseBody);
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
}