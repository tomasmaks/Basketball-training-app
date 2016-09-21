package com.example.tomas.becomebasketballpro;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.enums.Quality;
import com.thefinestartist.ytpa.utils.YouTubeThumbnail;
import com.squareup.picasso.Picasso;


public class BallTrainingThirdActivity extends Activity {

    JSONParser jsonParser = new JSONParser();

    JSONArray jsonArray = null;
    JSONArray exercises = null;

    String category_id = null;
    String exercise_id = null;

    String exercise_name, exercise_body;
    String exercise_video;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    boolean showAudioUi;
    boolean showFadeAnim;
    private boolean advertised = false;
    ImageButton play;
    ImageView thumbnail;

    private InterstitialAd mInterstitialAd;

    private static final String url_details = "https://firebasestorage.googleapis.com/v0/b/basketball-training-app.appspot.com/o/ListOfExercises.json?alt=media&token=724f8517-db16-4ad7-afb5-e48e9bf6d228";

    private static final String PARENT_ID = "ids";
    private static final String TABLE_EVENT = "Basketball";
    private static final String TAG_ARRAY = "exercises";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_BODY = "body";
    private static final String TAG_VIDEO = "video";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_balltraining_details);

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

        Intent i = getIntent();
        category_id = i.getStringExtra("category_id");
        exercise_id = i.getStringExtra("exercise_id");

        new LoadSingleExercise().execute();
    }

    class LoadSingleExercise extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(PARENT_ID, category_id));
            params.add(new BasicNameValuePair(TAG_ID, exercise_id));


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
                                BallTrainingModel ballTrainingModel = gson.fromJson(json.toString(), BallTrainingModel.class);

                                ballTrainingModel.setName(exercise_name);
                                exercise_name = nzn.getString(TAG_NAME);
                                ballTrainingModel.setBody(exercise_body);
                                exercise_body = Html.fromHtml(nzn.getString(TAG_BODY)).toString();
                                ballTrainingModel.setVideoURI(exercise_video);
                                exercise_video = nzn.getString(TAG_VIDEO);
                                ballTrainingModel.setIds(categoryId);
                                ballTrainingModel.setId(exerciseId);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            runOnUiThread(new Runnable() {
                public void run() {

                    TextView txt_exer_name = (TextView) findViewById(R.id.exercise_title);
                    txt_exer_name.setText(exercise_name);
                    TextView txt_exer_body = (TextView) findViewById(R.id.exercise_body);
                    txt_exer_body.setText(exercise_body);

                    setupVideoView();
                }
            });
        }
    }
    private void setupVideoView() {
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
                Intent intent = new Intent(BallTrainingThirdActivity.this, YouTubePlayerActivity.class);
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
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
