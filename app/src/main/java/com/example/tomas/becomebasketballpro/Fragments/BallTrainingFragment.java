package com.example.tomas.becomebasketballpro.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import com.example.tomas.becomebasketballpro.ArticleDetailsActivity;
import com.example.tomas.becomebasketballpro.DBHandler.ArticleDbHandler;
import com.example.tomas.becomebasketballpro.DBHandler.BallTrainingDbHandler;
import com.example.tomas.becomebasketballpro.DBHandler.FitnessDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.example.tomas.becomebasketballpro.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.message.BasicNameValuePair;
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
    ListAdapter adapter;
    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    List<BallTrainingModel> ballTrainingModelList;
    // albums JSONArray
    JSONArray categories = null;

    BallTrainingDbHandler dbHandler;
    List<BallTrainingModel> result = null;
    // albums JSON url
    private static final String URL_CATEGORIES = "https://raw.githubusercontent.com/tomasmaks/Basketball-training-app/master/app/json/ListOfExercises.json";

    // ALL JSON node names
    private static final String TAG_ID = "ids";
    private static final String TAG_NAME = "category";
    private static final String TAG_CATTHUM = "catThumb";
    private static final String TABLE_EVENT = "Basketball";

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
        mRootView = inflater.inflate(R.layout.fragment_balltraining, container, false);

        return mRootView;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        gridview = (GridView) mRootView.findViewById(R.id.list);

        dbHandler = new BallTrainingDbHandler(getActivity());

        NetworkUtils utils = new NetworkUtils(getActivity());
        if (savedInstanceState == null) {
            if (utils.isConnectingToInternet()) {

                new LoadCategories().execute();

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
            } else {
                result = dbHandler.getAllCategories();
                adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.fragment_balltraining_content, result);
                gridview.setAdapter(adapter);
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {

                        BallTrainingSecondFragment ballTrainingSecondFragment = new BallTrainingSecondFragment();

                        Bundle bundle = new Bundle();
                        String category_id = ((TextView) view.findViewById(R.id.category_id)).getText().toString();

                        bundle.putString("category_id", category_id);
                        ballTrainingSecondFragment.setArguments(bundle);
                        ((MainActivity) getActivity()).switchFragment(ballTrainingSecondFragment, false);
                    }
                });
            }
        }
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
    class LoadCategories extends AsyncTask<String, String, List<BallTrainingModel>> {

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
        protected List<BallTrainingModel> doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(URL_CATEGORIES, "GET",
                    param);

            try {

                List<BallTrainingModel> ballTrainingModelList = new ArrayList<>();
                Gson gson = new Gson();
                categories = json.getJSONArray(TABLE_EVENT);
                //categories = new JSONArray(json);

                dbHandler.deleteCategoryTable();
                // looping through All albums
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject c = categories.getJSONObject(i);
                    BallTrainingModel ballTrainingModel = gson.fromJson(json.toString(), BallTrainingModel.class);
                    ballTrainingModel.setIds(c.getString(TAG_ID));
                    ballTrainingModel.setCategory(c.getString(TAG_NAME));
                    ballTrainingModel.setCatThumb(c.getString(TAG_CATTHUM));
                    //ballTrainingCategoriesModel.setCount(c.getString(TAG_COUNT));
                    ballTrainingModelList.add(ballTrainingModel);
                    dbHandler.addCategory(ballTrainingModel);

                }
                return ballTrainingModelList;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(final List<BallTrainingModel> result) {
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.fragment_balltraining_content, result);
                    gridview.setAdapter(adapter);


                }
            });

        }

    }



    public class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        Context context;
        private List<BallTrainingModel> ballTrainingModelList;
        int resource;
        public ListAdapter(Context context, int resource, List<BallTrainingModel> ballTrainingModelList) {
            this.context = context;
            this.resource = resource;
            this.ballTrainingModelList = ballTrainingModelList;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        class ViewHolder {
            // Include Number of reqd views.
            private TextView ids;
            private TextView category;
            private ImageView tagThumb;


        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ballTrainingModelList.size();
        }

        @Override
        public Object getItem(int pos) {
            // TODO Auto-generated method stub
            return ballTrainingModelList.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            // TODO Auto-generated method stub
            return pos;
        }


        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder mViewHolder = null;
            if (view == null) {
                mViewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService
                        (Activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_balltraining_content, parent, false);
                mViewHolder.ids = (TextView) view.findViewById(R.id.category_id);
                mViewHolder.category = (TextView) view.findViewById(R.id.category_name);
                mViewHolder.tagThumb = (ImageView) view.findViewById(R.id.thumb);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }
            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(ballTrainingModelList.get(position).getCatThumb(), mViewHolder.tagThumb);

            mViewHolder.ids.setText(ballTrainingModelList.get(position).getIds());
            mViewHolder.category.setText(ballTrainingModelList.get(position).getCategory());

            //mViewHolder.count.setText(ballTraininCategoriesModelList.get(position).getCount());

            return view;
        }
    }

}




