package com.example.tomas.becomebasketballpro;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.example.tomas.becomebasketballpro.Model.JSONParser;

public class BallTrainingSingleActivity extends Activity {

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    // tracks JSONArray
    JSONArray jsonArray = null;
    JSONArray songs = null;
    // Album id
    String album_id = null;
    String song_id = null;

    String song_name,  duration;

    // single song JSON url
    // GET parameters album, song
    private static final String url_details = "https://gist.githubusercontent.com/tomasmaks/bc2eddf95f05a6c93c57bc8d6886b061/raw/4c296970f7f2de04c706d023c4125f59bc614752/album_tracks.json";

    // ALL JSON node names
    private static final String PARENT_ID = "ids";
    private static final String TABLE_EVENT = "Basketball";
    private static final String TAG_ARRAY = "songs";
    private static final String SONG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_ALBUM = "album";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_balltraining_details);

        // Get album id, song id
        Intent i = getIntent();
        song_id = i.getStringExtra("song_id");
        album_id = i.getStringExtra("album_id");
        Log.d("TAG", album_id.toString());
        Log.d("TAG2", song_id.toString());

        // calling background thread
        new LoadSingleTrack().execute();
    }

    /**
     * Background Async Task to get single song information
     * */
    class LoadSingleTrack extends AsyncTask<String, String, String> {

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
            params.add(new BasicNameValuePair(PARENT_ID, album_id));
            params.add(new BasicNameValuePair(SONG_ID, song_id));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_details, "GET",
                    params);

            try {
                jsonArray = json.getJSONArray(TABLE_EVENT);
                Log.d(TAG_ALBUM, jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject details = jsonArray.getJSONObject(i);
                    String albumId = details.getString(PARENT_ID);
                    Log.d(TAG_ARRAY, details.toString());
                    if (albumId.equals(album_id)) {


                        songs = details.getJSONArray(TAG_ARRAY);

                        for (int j = 0; j < songs.length(); j++) {
                            JSONObject nzn = songs.getJSONObject(j);
                            String songId = nzn.getString(SONG_ID);
                            if (songId.equals(song_id)) {
                                song_name = nzn.getString(TAG_NAME);
                                duration = nzn.getString(TAG_DURATION);
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

                    TextView txt_song_name = (TextView) findViewById(R.id.song_title);
                    TextView txt_duration = (TextView) findViewById(R.id.duration);

                    // displaying song data in view
                    txt_song_name.setText(song_name);
                    txt_duration.setText(duration);

                    // Change Activity Title with Song title
                    setTitle(song_name);
                }
            });

        }

    }
}
