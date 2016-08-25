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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.google.gson.Gson;

public class BallTrainingThirdActivity extends Activity implements OnPreparedListener {

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    JSONArray jsonArray = null;
    JSONArray exercises = null;

    String category_id = null;
    String exercise_id = null;

    String exercise_description, exercise_name, exercise_body;
    String exercise_video;

    // single song JSON url
    // GET parameters album, song
    private static final String url_details = "https://gist.githubusercontent.com/tomasmaks/bc2eddf95f05a6c93c57bc8d6886b061/raw/e536d4bf271221d99c28f322231195536c90e83f/ListOfExercises.json";
    com.devbrackets.android.exomedia.ui.widget.EMVideoView emVideoView;
    // ALL JSON node names
    private static final String PARENT_ID = "ids";
    private static final String TABLE_EVENT = "Basketball";
    private static final String TAG_ARRAY = "exercises";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_BODY = "body";
    private static final String TAG_VIDEO = "video";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_balltraining_details);

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
                                BallTrainingModel ballTrainingModel = gson.fromJson(json.toString(), BallTrainingModel.class);

                                ballTrainingModel.setName(exercise_name);
                                exercise_name = nzn.getString(TAG_NAME);
                                ballTrainingModel.setDescription(exercise_description);
                                exercise_description = nzn.getString(TAG_DESCRIPTION);
                                ballTrainingModel.setBody(exercise_body);
                                exercise_body = nzn.getString(TAG_BODY);
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
                    TextView txt_exer_description = (TextView) findViewById(R.id.exercise_description);
                    txt_exer_description.setText(exercise_description);
                    TextView txt_exer_body = (TextView) findViewById(R.id.exercise_body);
                    txt_exer_body.setText(exercise_body);


                    setupVideoView();

                    emVideoView = (com.devbrackets.android.exomedia.ui.widget.EMVideoView) findViewById(R.id.video_view);
                    emVideoView.setVideoURI(Uri.parse(exercise_video));

                    onPrepared();
                }
            });

        }

    }
    private void setupVideoView() {
        emVideoView = (EMVideoView)findViewById(R.id.video_view);
        emVideoView.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        emVideoView.setVideoURI(Uri.parse(exercise_video));
    }

    @Override
    public void onPrepared() {
        //Starts the video playback as soon as it is ready
        emVideoView.start();
    }
}
