package com.example.tomas.becomebasketballpro.Fragments;

import android.support.v4.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.Model.JSONParserString;
import com.example.tomas.becomebasketballpro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tomas on 11/08/2016.
 */
public class BallTrainingFragment extends ListFragment {
    View mRootView;
    GridView gridview;
    // Creating JSON Parser object
    JSONParserString jsonParser = new JSONParserString();

    ArrayList<HashMap<String, String>> categoryList;

    // albums JSONArray
    JSONArray categories = null;

    // albums JSON url
    private static final String URL_CATEGORIES = "https://gist.githubusercontent.com/tomasmaks/c9a92ab502f69514b923128cb1af0910/raw/65ed806dfdec5ee07d624d047d3ce8f34aed66d0/category.json";

    // ALL JSON node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_COUNT = "count";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_balltraining, container, false);

        return mRootView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {

        categoryList = new ArrayList<HashMap<String, String>>();

        new LoadCategories().execute();


        gridview = (GridView) mRootView.findViewById(R.id.list);

        gridview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {

                BallTrainingSecondFragment ballTrainingSecondFragment = new BallTrainingSecondFragment();
                Bundle bundle = new Bundle();

                String category_id = ((TextView) view.findViewById(R.id.category_id)).getText().toString();
                bundle.putString("category_id", category_id);

                ballTrainingSecondFragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(ballTrainingSecondFragment, false);
            }
        });
    }


    public static BallTrainingFragment newInstance(int sectionNumber) {
        BallTrainingFragment fragment = new BallTrainingFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BallTrainingFragment() {
        // Required empty public constructor
    }

    /**
     * Background Async Task to Load all Albums by making http request
     */
    class LoadCategories extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            String json = jsonParser.makeHttpRequest(URL_CATEGORIES, "GET",
                    params);

            try {
                categories = new JSONArray(json);

                if (categories != null) {
                    // looping through All albums
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject c = categories.getJSONObject(i);

                        // Storing each json item values in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String count = c.getString(TAG_COUNT);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_COUNT, count);

                        // adding HashList to ArrayList
                        categoryList.add(map);
                    }
                } else {
                    Log.d("Categories: ", "null");
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
                            getActivity(), categoryList,
                            R.layout.fragment_balltraining_content, new String[]{TAG_ID,
                            TAG_NAME, TAG_COUNT}, new int[]{
                            R.id.category_id, R.id.category_name, R.id.count});

                    gridview.setAdapter(adapter);
                }
            });

        }

    }

}