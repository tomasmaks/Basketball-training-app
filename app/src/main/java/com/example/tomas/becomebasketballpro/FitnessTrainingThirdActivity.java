package com.example.tomas.becomebasketballpro;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubePlayer;
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

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    JSONArray jsonArray = null;
    JSONArray exercises = null;

    String category_id = null;
    String exercise_id = null;

    String exercise_description, exercise_name, exercise_body;
    String exercise_video;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    private static String VIDEO_ID = "iS1g8G_njx8";
    boolean showAudioUi;
    boolean showFadeAnim;
    private boolean advertised = false;
    ImageButton play;
    ImageView thumbnail;

    private InterstitialAd mInterstitialAd;


    // single song JSON url
    // GET parameters album, song
    private static final String url_details = "https://raw.githubusercontent.com/tomasmaks/Basketball-training-app/master/app/json/ListOfFitnessExercises.json";

    // ALL JSON node names
    private static final String PARENT_ID = "ids";
    private static final String TABLE_EVENT = "Fitness";
    private static final String TAG_ARRAY = "exercises";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_BODY = "body";
    private static final String TAG_VIDEO = "video";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fitnesstraining_details);

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

        // Get album id, song id
        Intent i = getIntent();
        category_id = i.getStringExtra("category_id");
        exercise_id = i.getStringExtra("exercise_id");

        // calling background thread
        new LoadSingleExercise().execute();
    }

    /**
     * Background Async Task to get single song information
     * */
    class LoadSingleExercise extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting song json and parsing
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // post album id, song id as GET parameters
            params.add(new BasicNameValuePair(PARENT_ID, category_id));
            params.add(new BasicNameValuePair(TAG_ID, exercise_id));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_details, "GET",
                    params);

            try {
                Gson gson = new Gson();

                jsonArray = json.getJSONArray(TABLE_EVENT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject details = jsonArray.getJSONObject(i);

                    String categoryId = details.getString(PARENT_ID);

                    if (categoryId.equals(category_id)) {


                        exercises = details.getJSONArray(TAG_ARRAY);


                        for (int j = 0; j < exercises.length(); j++) {
                            JSONObject nzn = exercises.getJSONObject(j);
                            String exerciseId = nzn.getString(TAG_ID);
                            if (exerciseId.equals(exercise_id)) {
                                FitnessTrainingModel fitnessTrainingModel = gson.fromJson(json.toString(), FitnessTrainingModel.class);

                                fitnessTrainingModel.setName(exercise_name);
                                exercise_name = nzn.getString(TAG_NAME);
                                fitnessTrainingModel.setBody(exercise_body);
                                exercise_body = nzn.getString(TAG_BODY);
                                fitnessTrainingModel.setVideoURI(exercise_video);
                                exercise_video = nzn.getString(TAG_VIDEO);



                                fitnessTrainingModel.setIds(categoryId);
                                fitnessTrainingModel.setId(exerciseId);

                            }

                        }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting song information

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    TextView txt_exer_name = (TextView) findViewById(R.id.exercise_title);
                    txt_exer_name.setText(exercise_name);
                    TextView txt_exer_body = (TextView) findViewById(R.id.exercise_body);
                    txt_exer_body.setText(exercise_body);


                    setupVideoView();

//                    emVideoView = (com.devbrackets.android.exomedia.ui.widget.EMVideoView) findViewById(R.id.video_view);
//                    emVideoView.setVideoURI(Uri.parse("http://www.youtube.com/v/VNv3EZEUgok?autohide=1&version=3"));


                }
            });

        }

    }
    private void setupVideoView() {
//        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
//
//        // Initializing video player with developer key
//        youTubeView.initialize(Config.DEVELOPER_KEY, this);
        playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        orientation = Orientation.AUTO;
        showAudioUi = true;
        showFadeAnim = true;

        play = (ImageButton) findViewById(R.id.play_bt);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        Picasso.with(this)
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
//                if (showFadeAnim) {
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.fade_in);
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.fade_out);
//                } else {
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.modal_close_enter);
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.modal_close_exit);
//                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
