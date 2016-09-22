package com.example.tomas.becomebasketballpro.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;

import com.example.tomas.becomebasketballpro.DBHandler.MotivationDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;

import com.example.tomas.becomebasketballpro.Model.MotivationModel;
import com.example.tomas.becomebasketballpro.MotivationDetailsActivity;
import com.example.tomas.becomebasketballpro.R;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 09/08/2016.
 */
public class MotivationListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View mRootView;
    ListView mListView;
    private String URL_TO_HIT = "https://raw.githubusercontent.com/tomasmaks/Basketball-training-app/master/app/json/Motivation.json";
    MotivationAdapter adapter;
    private SwipeRefreshLayout refreshLayout = null;
    MotivationDbHandler dbHandler;
    List<MotivationModel> result = null;

    public static MotivationListFragment newInstance(int sectionNumber) {
        MotivationListFragment fragment = new MotivationListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public MotivationListFragment() {
    }

    public void LoadMotivation() {

        new FetchMotivationTask().execute(URL_TO_HIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            LoadMotivation();

        }

        dbHandler = new MotivationDbHandler(getActivity());

        mRootView = inflater.inflate(R.layout.fragment_motivation_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.mListView);

        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.green));
        refreshLayout.setOnRefreshListener(this);


        NetworkUtils utils = new NetworkUtils(getActivity());
        if (!utils.isConnectingToInternet() && savedInstanceState == null) {

            result = dbHandler.getAllMotivation();
            adapter = new MotivationAdapter(getActivity().getApplicationContext(), R.layout.fragment_motivation_list_items, result);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                    MotivationModel motivationModel = result.get(position2);
                    Intent intent = new Intent(getActivity(), MotivationDetailsActivity.class);
                    intent.putExtra("motivationModel", new Gson().toJson(motivationModel));
                    getActivity().startActivity(intent);
                }
            });

        }
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(0);
            }
        }).start();


    }

    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    new FetchMotivationTask().execute(URL_TO_HIT);
                    refreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    }


    public class FetchMotivationTask extends AsyncTask<String, String, List<MotivationModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<MotivationModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("Motivation");

                List<MotivationModel> motivationModelList = new ArrayList<>();

                Gson gson = new Gson();

                dbHandler.deleteTable();

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself which is commented below
                     */
                    MotivationModel motivationModel = gson.fromJson(finalObject.toString(), MotivationModel.class);
                    motivationModel.setThumbnail(finalObject.getString("thumb"));
                    motivationModel.setImage(finalObject.getString("photo"));

                    motivationModelList.add(motivationModel);
                    dbHandler.addMotivation(motivationModel);
                }
                return motivationModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(final List<MotivationModel> result) {
            super.onPostExecute(result);

            if (result != null) {
                adapter = new MotivationAdapter(getActivity().getApplicationContext(), R.layout.fragment_motivation_list_items, result);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MotivationModel motivationModel = result.get(position);
                        Intent intent = new Intent(getActivity(), MotivationDetailsActivity.class);
                        intent.putExtra("motivationModel", new Gson().toJson(motivationModel));
                        getActivity().startActivity(intent);

                    }
                });
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MotivationAdapter extends ArrayAdapter {

        private List<MotivationModel> motivationModelList;
        private int resource;
        private LayoutInflater inflater;

        public MotivationAdapter(Context context, int resource, List<MotivationModel> objects) {
            super(context, resource, objects);
            motivationModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(getActivity()).load(motivationModelList.get(position).getThumbnail()).into(holder.thumbnail);

            return convertView;
        }


        class ViewHolder {
            private ImageView thumbnail;
        }

    }




}
