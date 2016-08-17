package com.example.tomas.becomebasketballpro;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tomas.becomebasketballpro.Model.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tomas on 14/08/2016.
 */
public class BallTrainingDetailsActivity extends ListActivity {

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> tracksList;

    // tracks JSONArray
    JSONArray Albums = null;

    // Album id
    String album_id, album_name;

    // tracks JSON url
    // id - should be posted as GET params to get track list (ex: id = 5)
    private String url_details = "https://gist.githubusercontent.com/tomasmaks/bc2eddf95f05a6c93c57bc8d6886b061/raw/b2c85a907c6c4ad8b3e03e3ea748dd6fe606d121/album_tracks.json";

    // ALL JSON node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ALBUM = "album";
    private static final String TAG_DURATION = "duration";
    private static final String TABLE_EVENT = "BasketballTraining";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_balltraining_list);
        // Get album id
        Intent i = getIntent();
        album_id = i.getStringExtra("album_id");

        // Hashmap for ListView
        tracksList = new ArrayList<HashMap<String, String>>();

        // Loading tracks in Background Thread
        new LoadTracks().execute();

        // get listview
        ListView lv = getListView();

        /**
         * Listview on item click listener
         * SingleTrackActivity will be lauched by passing album id, song id
         * */
        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // On selecting single track get song information
                Intent i = new Intent(getApplicationContext(), BallTrainingSingleActivity.class);

                // to get song information
                // both album id and song is needed
                String album_id = ((TextView) view.findViewById(R.id.album_id)).getText().toString();
                String song_id = ((TextView) view.findViewById(R.id.song_id)).getText().toString();

                Toast.makeText(getApplicationContext(), "Album Id: " + album_id + ", Song Id: " + song_id, Toast.LENGTH_SHORT).show();

                i.putExtra("album_id", album_id);
                i.putExtra("song_id", song_id);

                startActivity(i);
            }
        });

    }

    /**
     * Background Async Task to Load all tracks under one album
     */
    class LoadTracks extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting tracks json and parsing
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_details,
                    "GET", params);

            try {
                // products found
                // Getting Array of Products
                Albums = json.getJSONArray(TABLE_EVENT);
                // looping through All Contacts
                for (int i = 0; i < Albums.length(); i++) {
                    JSONObject evt = Albums.getJSONObject(i);

                    // Storing each json item in variable
                    String id = evt.getString(TAG_ID);
                    String album = evt.getString(TAG_ALBUM);
                    //String name = evt.getString(TAG_NAME);
                    String duration = evt.getString(TAG_DURATION);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_ID, id);
                    map.put(TAG_ALBUM, album);
                   // map.put(TAG_NAME, name);
                    map.put(TAG_DURATION, duration);

                    // adding HashList to ArrayList
                    tracksList.add(map);
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
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            BallTrainingDetailsActivity.this, tracksList,
                            R.layout.fragment_balltraining_list_items, new String[]{album_id, TAG_ID, TAG_NAME,
                            TAG_DURATION}, new int[]{
                            R.id.album_id, R.id.song_id, R.id.album_name, R.id.song_duration});
                    // updating listview
                    setListAdapter(adapter);

                    // Change Activity Title with Album name
                    setTitle(album_name);
                }
            });

        }
    }
}