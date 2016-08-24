package com.example.tomas.becomebasketballpro.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.TextView;

import org.apache.http.NameValuePair;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.Model.BallTrainingCategoriesModel;
import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.example.tomas.becomebasketballpro.Model.JSONParserString;
import com.example.tomas.becomebasketballpro.R;
import com.google.gson.Gson;

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

    List<BallTrainingModel> ballTrainingCategoriesModelList;
    // albums JSONArray
    JSONArray categories = null;

    // albums JSON url
    private static final String URL_CATEGORIES = "https://gist.githubusercontent.com/tomasmaks/bc2eddf95f05a6c93c57bc8d6886b061/raw/561055b8be24ee3f458d904f23d8e67a83e97bf3/ListOfExercises.json";

    // ALL JSON node names
    private static final String TAG_ID = "ids";
    private static final String TAG_NAME = "category";
    //private static final String TAG_COUNT = "count";
    private static final String TABLE_EVENT = "Basketball";

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
    public void onViewCreated(View view, Bundle savedInstanceState) {

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

                List<BallTrainingModel> ballTrainingCategoriesModelList = new ArrayList<>();
                Gson gson = new Gson();
                categories = json.getJSONArray(TABLE_EVENT);
                //categories = new JSONArray(json);
                BallTrainingModel ballTrainingCategoriesModel = gson.fromJson(json.toString(), BallTrainingModel.class);

                    // looping through All albums
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject c = categories.getJSONObject(i);

                        ballTrainingCategoriesModel.setId(c.getString(TAG_ID));
                        ballTrainingCategoriesModel.setName(c.getString(TAG_NAME));
                        //ballTrainingCategoriesModel.setCount(c.getString(TAG_COUNT));

                        ballTrainingCategoriesModelList.add(ballTrainingCategoriesModel);
                }
                return ballTrainingCategoriesModelList;

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
                    /**
                     * Updating parsed JSON data into ListView
                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            getActivity(), categoryList,
//                            R.layout.fragment_balltraining_content, new String[]{TAG_ID,
//                            TAG_NAME, TAG_COUNT}, new int[]{
//                            R.id.category_id, R.id.category_name, R.id.count});
//
//                    gridview.setAdapter(adapter);

                    adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.fragment_balltraining_list_items, result);
                    setListAdapter(adapter);


                }
            });

        }

    }



    public class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        Context context;
        private List<BallTrainingModel> ballTraininCategoriesModelList;
        int resource;
    public ListAdapter(Context context, int resource, List<BallTrainingModel> ballTraininCategoriesgModelList) {
        this.context = context;
        this.resource = resource;
        this.ballTraininCategoriesModelList = ballTraininCategoriesgModelList;
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder {
        // Include Number of reqd views.
        private TextView ids;
        private TextView category;
       // private TextView count;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ballTraininCategoriesModelList.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return ballTraininCategoriesModelList.get(pos);
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
            view = inflater.inflate(R.layout.fragment_balltraining_list_items, parent, false);
            mViewHolder.ids = (TextView) view.findViewById(R.id.category_id);
            mViewHolder.category = (TextView) view.findViewById(R.id.category_name);
            //mViewHolder.count = (TextView) view.findViewById(R.id.count);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        // Then later, when you want to display image
        //ImageLoader.getInstance().displayImage(ballTrainingModelList.get(position).getThumb(), mViewHolder.thumb);

        mViewHolder.category.setText(ballTraininCategoriesModelList.get(position).getCategory());
        mViewHolder.ids.setText(ballTraininCategoriesModelList.get(position).getIds());
        //mViewHolder.count.setText(ballTraininCategoriesModelList.get(position).getCount());

        return view;
    }
}

}




