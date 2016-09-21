package com.example.tomas.becomebasketballpro.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomas.becomebasketballpro.DBHandler.FitnessDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.example.tomas.becomebasketballpro.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 05/09/2016.
 */
public class FitnessTrainingFragment extends ListFragment {

    View mRootView;
    GridView gridview;
    ListAdapter adapter;

    JSONParser jsonParser = new JSONParser();

    List<FitnessTrainingModel> fitnessTrainingModelList;

    JSONArray categories = null;

    FitnessDbHandler dbHandler;
    List<FitnessTrainingModel> result;

    private static final String URL_CATEGORIES = "https://raw.githubusercontent.com/tomasmaks/Basketball-training-app/master/app/json/ListOfFitnessExercises.json";

    private static final String TAG_ID = "ids";
    private static final String TAG_NAME = "category";
    private static final String TAG_CATTHUM = "catThumb";
    private static final String TABLE_EVENT = "Fitness";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = inflater.inflate(R.layout.fragment_fitnesstraining, container, false);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        gridview = (GridView) mRootView.findViewById(R.id.list);

        dbHandler = new FitnessDbHandler(getActivity());

        NetworkUtils utils = new NetworkUtils(getActivity());
        if (savedInstanceState == null) {
            if (utils.isConnectingToInternet()) {

                new LoadCategories().execute();
                gridview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                            long arg3) {

                        FitnessTrainingSecondFragment fitnessTrainingSecondFragment = new FitnessTrainingSecondFragment();
                        Bundle bundle = new Bundle();

                        String category_id = ((TextView) view.findViewById(R.id.category_id)).getText().toString();
                        bundle.putString("category_id", category_id);

                        fitnessTrainingSecondFragment.setArguments(bundle);
                        ((MainActivity) getActivity()).switchFragment(fitnessTrainingSecondFragment, false);
                    }
                });


            } else {
                result = dbHandler.getAllCategories();
                adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.fragment_fitnesstraining_content, result);
                gridview.setAdapter(adapter);
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {

                        FitnessTrainingSecondFragment fitnessTrainingSecondFragment = new FitnessTrainingSecondFragment();

                        Bundle bundle = new Bundle();
                        String category_id = ((TextView) view.findViewById(R.id.category_id)).getText().toString();

                        bundle.putString("category_id", category_id);
                        fitnessTrainingSecondFragment.setArguments(bundle);
                        ((MainActivity) getActivity()).switchFragment(fitnessTrainingSecondFragment, false);
                    }
                });
            }
        }
    }

    public static FitnessTrainingFragment newInstance(int sectionNumber) {
        FitnessTrainingFragment fragment = new FitnessTrainingFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FitnessTrainingFragment() {
        // Required empty public constructor
    }

    class LoadCategories extends AsyncTask<String, String, List<FitnessTrainingModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected List<FitnessTrainingModel> doInBackground(String... args) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();

            JSONObject json = jsonParser.makeHttpRequest(URL_CATEGORIES, "GET",
                    param);

            try {

                List<FitnessTrainingModel> fitnessTrainingModelList = new ArrayList<>();
                Gson gson = new Gson();
                categories = json.getJSONArray(TABLE_EVENT);
                dbHandler.deleteCategoryTable();

                for (int i = 0; i < categories.length(); i++) {
                    JSONObject c = categories.getJSONObject(i);
                    FitnessTrainingModel fitnessTrainingModel = gson.fromJson(json.toString(), FitnessTrainingModel.class);
                    fitnessTrainingModel.setIds(c.getString(TAG_ID));
                    fitnessTrainingModel.setCategory(c.getString(TAG_NAME));
                    fitnessTrainingModel.setCatThumb(c.getString(TAG_CATTHUM));
                    fitnessTrainingModelList.add(fitnessTrainingModel);
                    dbHandler.addCategory(fitnessTrainingModel);

                }
                return fitnessTrainingModelList;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(final List<FitnessTrainingModel> result) {

            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.fragment_fitnesstraining_content, result);
                    gridview.setAdapter(adapter);
                }
            });

        }

    }



    public class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        Context context;
        private List<FitnessTrainingModel> fitnessTrainingModelList;
        int resource;
        public ListAdapter(Context context, int resource, List<FitnessTrainingModel> fitnessTrainingModelList) {
            this.context = context;
            this.resource = resource;
            this.fitnessTrainingModelList = fitnessTrainingModelList;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        class ViewHolder {
            private TextView ids;
            private TextView category;
            private ImageView tagThumb;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fitnessTrainingModelList.size();
        }

        @Override
        public Object getItem(int pos) {
            // TODO Auto-generated method stub
            return fitnessTrainingModelList.get(pos);
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
                view = inflater.inflate(R.layout.fragment_fitnesstraining_content, parent, false);
                mViewHolder.ids = (TextView) view.findViewById(R.id.category_id);
                mViewHolder.category = (TextView) view.findViewById(R.id.category_name);
                mViewHolder.tagThumb = (ImageView) view.findViewById(R.id.thumb);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }

            Picasso.with(getActivity()).load(fitnessTrainingModelList.get(position).getCatThumb()).into(mViewHolder.tagThumb);

            mViewHolder.ids.setText(fitnessTrainingModelList.get(position).getIds());
            mViewHolder.category.setText(fitnessTrainingModelList.get(position).getCategory());

            return view;
        }
    }
}