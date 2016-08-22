package com.example.tomas.becomebasketballpro.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.tomas.becomebasketballpro.BallTrainingThirdActivity;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.example.tomas.becomebasketballpro.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tomas on 14/08/2016.
 */
public class BallTrainingSecondFragment extends ListFragment {
    View mRootView;
    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> exercisesList;

    // tracks JSONArray
    JSONArray Categories = null;
    JSONArray Exercises = null;

    // Album id
    String category_ids;

    // tracks JSON url
    // id - should be posted as GET params to get track list (ex: id = 5)
    String url_details = "https://gist.githubusercontent.com/tomasmaks/bc2eddf95f05a6c93c57bc8d6886b061/raw/561055b8be24ee3f458d904f23d8e67a83e97bf3/ListOfExercises.json";

    // ALL JSON node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_THUMB = "thumb";
    private static final String TABLE_EVENT = "Basketball";
    private static final String TAG_ARRAY = "exercises";
    private static final String PARENT_ID = "ids";

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {

        category_ids = getArguments().getString("category_id");

        // Hashmap for ListView
        exercisesList = new ArrayList<HashMap<String, String>>();

        // Loading tracks in Background Thread
        new LoadExercises().execute();

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
                Intent i = new Intent(getActivity().getApplicationContext(), BallTrainingThirdActivity.class);

                String category_id = ((TextView) view.findViewById(R.id.category_id)).getText().toString();
                String exercise_id = ((TextView) view.findViewById(R.id.exercise_id)).getText().toString();

                // to get song information
                // both album id and song is needed
                i.putExtra("category_id", category_id);
                i.putExtra("exercise_id", exercise_id);

                view.getContext().startActivity(i);
            }
        });

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_balltraining_list, container, false);

        return mRootView;
    }

    /**
     * Background Async Task to Load all tracks under one album
     */
    class LoadExercises extends AsyncTask<String, String, String> {

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


// post album id as GET parameter
            params.add(new BasicNameValuePair(PARENT_ID, category_ids));

            // post album id as GET parameter
//            params.add(new BasicNameValuePair(TAG_ID, album_id));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_details,
                    "GET", params);

            try {
                // products found
                // Getting Array of Products
                Categories = json.getJSONArray(TABLE_EVENT);
                // looping through All Contacts
                for (int i = 0; i < Categories.length(); i++) {
                    JSONObject evt = Categories.getJSONObject(i);

                    // Storing each json item in variable
                    String category_id = evt.getString(PARENT_ID);
                    //String album_name = evt.getString(TAG_ALBUM);
                    if (category_id.equals(category_ids)) {

                        Exercises = evt.getJSONArray(TAG_ARRAY);

                        for (int j = 0; j < Exercises.length(); j++) {
                            JSONObject nzn = Exercises.getJSONObject(j);

                            String exercise_id = nzn.getString(TAG_ID);
                            String name = nzn.getString(TAG_NAME);
                            String thumb = nzn.getString(TAG_THUMB);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TAG_ID, exercise_id);
                            map.put(PARENT_ID, category_id);
                            map.put(TAG_NAME, name);
                            map.put(TAG_THUMB, thumb);

                            exercisesList.add(map);

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
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(), exercisesList,
                            R.layout.fragment_balltraining_list_items, new String[]{TAG_ID, PARENT_ID, TAG_NAME, TAG_THUMB}, new int[]{
                            R.id.exercise_id, R.id.category_id, R.id.name, R.id.thumb_image});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }
    }
}