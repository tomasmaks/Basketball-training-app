package com.example.tomas.becomebasketballpro.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.BallTrainingThirdActivity;
import com.example.tomas.becomebasketballpro.DBHandler.BallTrainingDbHandler;
import com.example.tomas.becomebasketballpro.DBHandler.FitnessDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.example.tomas.becomebasketballpro.R;
import com.example.tomas.becomebasketballpro.ui.ToastAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 14/08/2016.
 */
public class BallTrainingSecondFragment extends ListFragment {
    View mRootView;
    ListAdapter adapter;
    Context context;
    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    //ArrayList<HashMap<String, String>> exercisesList;
    List<BallTrainingModel> ballTrainingModelList;

    // tracks JSONArray
    JSONArray Categories = null;
    JSONArray Exercises = null;

    // Album id
    String category_ids;

    private AdView mAdView;

    BallTrainingDbHandler dbHandler;
    List<BallTrainingModel> result = null;

    // tracks JSON url
    // id - should be posted as GET params to get track list (ex: id = 5)
    String url_details = "https://raw.githubusercontent.com/tomasmaks/Basketball-training-app/master/app/json/ListOfExercises.json";

    // ALL JSON node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_THUMB = "thumb";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TABLE_EVENT = "Basketball";
    private static final String TAG_ARRAY = "exercises";
    private static final String PARENT_ID = "ids";

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {

        category_ids = getArguments().getString("category_id");

        // Hashmap for ListView
        //ballTrainingModelList = new ArrayList<HashMap<String, String>>();
        dbHandler = new BallTrainingDbHandler(getActivity());

        NetworkUtils utils = new NetworkUtils(getActivity());
        if (utils.isConnectingToInternet() && savedInstanceState == null) {

            new LoadExercises().execute();

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

        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Please connect to internet to see ball training list", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

        mAdView = (AdView) mRootView.findViewById(R.id.adView);
        // Set the AdListener before building or loading the AdRequest.
        mAdView.setAdListener(new ToastAdListener(getActivity()));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return mRootView;
    }

    /**
     * Background Async Task to Load all tracks under one album
     */
    class LoadExercises extends AsyncTask<String, String, List<BallTrainingModel>> {

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
        protected List<BallTrainingModel> doInBackground(String... params) {
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();

//
//           post album id as GET parameter
            param.add(new BasicNameValuePair(PARENT_ID, category_ids));
//
//            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_details,
                    "GET", param);

            try {

                List<BallTrainingModel> ballTrainingModelList = new ArrayList<>();
                Gson gson = new Gson();

                Categories = json.getJSONArray(TABLE_EVENT);

                for(int i=0; i < Categories.length(); i++) {
                    JSONObject finalObject = Categories.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself which is commented below
                     */
                    String category_Id = finalObject.getString(PARENT_ID);


                    if (category_Id.equals(category_ids)) {

                        Exercises = finalObject.getJSONArray(TAG_ARRAY);

                        for (int j = 0; j < Exercises.length(); j++) {

                            JSONObject finalObject2 = Exercises.getJSONObject(j);

                            BallTrainingModel ballTrainingModel = gson.fromJson(json.toString(), BallTrainingModel.class);

                            ballTrainingModel.setId(finalObject2.getString(TAG_ID));
                            ballTrainingModel.setName(finalObject2.getString(TAG_NAME));
                            ballTrainingModel.setDescription(finalObject2.getString(TAG_DESCRIPTION));
                            ballTrainingModel.setThumb(finalObject2.getString(TAG_THUMB));
                            ballTrainingModel.setIds(category_Id);
                            ballTrainingModelList.add(ballTrainingModel);
                        }

                    }
                }
                return ballTrainingModelList;

            }  catch (JSONException e) {
                e.printStackTrace();

            }
            return  null;

        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(final List<BallTrainingModel> result) {
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    adapter = new ListAdapter(getActivity(), result);
                    setListAdapter(adapter);

                }
            });

        }
    }
    public class ListAdapter extends BaseAdapter {
        Context context;
        private List<BallTrainingModel> ballTrainingModelList;

        public ListAdapter(Context context,List<BallTrainingModel> ballTrainingModelList){
            this.context = context;
            this.ballTrainingModelList = ballTrainingModelList;
        }

        class ViewHolder {
            // Include Number of reqd views.
            private TextView ids;
            private TextView id;
            private TextView name;
            private TextView description;
            private ImageView thumb;


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
            if(view == null){
                mViewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService
                        (Activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_balltraining_list_items, parent, false);
                mViewHolder.ids = (TextView)view.findViewById(R.id.category_id);
                mViewHolder.id = (TextView)view.findViewById(R.id.exercise_id);
                mViewHolder.name = (TextView)view.findViewById(R.id.name);
                mViewHolder.description = (TextView)view.findViewById(R.id.description);
                mViewHolder.thumb=(ImageView)view.findViewById(R.id.thumb_image);
                view.setTag(mViewHolder);
            }
            else {
                mViewHolder = (ViewHolder) view.getTag();
            }
            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(ballTrainingModelList.get(position).getThumb(), mViewHolder.thumb);

            mViewHolder.name.setText(ballTrainingModelList.get(position).getName());
            mViewHolder.description.setText(ballTrainingModelList.get(position).getDescription());
            mViewHolder.ids.setText(ballTrainingModelList.get(position).getIds());
            mViewHolder.id.setText(ballTrainingModelList.get(position).getId());

            return view;
        }

    }
}