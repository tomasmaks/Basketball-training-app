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
//
//    // Creating JSON Parser object
//    JSONParser jsonParser = new JSONParser();
//
//    // tracks JSONArray
//    JSONArray albums = null;
//
//    // Album id
//    String album_id = null;
//    String song_id = null;
//
//    String album_name, song_name, duration;
//
//    // single song JSON url
//    // GET parameters album, song
//    private static final String URL_SONG = "https://gist.githubusercontent.com/tomasmaks/9112b82f2f0997d8596c067c1595fd12/raw/80edb031e66c1a7823e076e4a07bda70c5049adb/track";
//
//    // ALL JSON node names
//    private static final String TAG_NAME = "name";
//    private static final String TAG_DURATION = "duration";
//    private static final String TAG_ALBUM = "album";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_balltraining_details);
//
//        // Get album id, song id
//        Intent i = getIntent();
//        album_id = i.getStringExtra("album_id");
//        song_id = i.getStringExtra("song_id");
//
//        // calling background thread
//        new LoadSingleTrack().execute();
//    }
//
//    /**
//     * Background Async Task to get single song information
//     * */
//    class LoadSingleTrack extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        /**
//         * getting song json and parsing
//         * */
//        protected String doInBackground(String... args) {
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//            // post album id, song id as GET parameters
//            params.add(new BasicNameValuePair("album", album_id));
//            params.add(new BasicNameValuePair("song", song_id));
//
//            // getting JSON string from URL
//            String json = jsonParser.makeHttpRequest(URL_SONG, "GET",
//                    params);
//
//            // Check your log cat for JSON reponse
//            Log.d("Single Track JSON: ", json);
//
//            try {
//                JSONObject jObj = new JSONObject(json);
//                if(jObj != null){
//                    song_name = jObj.getString(TAG_NAME);
//                    album_name = jObj.getString(TAG_ALBUM);
//                    duration = jObj.getString(TAG_DURATION);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after getting song information
//
//            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//
//                    TextView txt_song_name = (TextView) findViewById(R.id.song_title);
//                    TextView txt_album_name = (TextView) findViewById(R.id.album_name);
//                    TextView txt_duration = (TextView) findViewById(R.id.duration);
//
//                    // displaying song data in view
//                    txt_song_name.setText(song_name);
//                    txt_album_name.setText(Html.fromHtml("<b>Album:</b> " + album_name));
//                    txt_duration.setText(Html.fromHtml("<b>Duration:</b> " + duration));
//
//                    // Change Activity Title with Song title
//                    setTitle(song_name);
//                }
//            });
//
//        }
//
//    }
//




//
//
}
