package com.example.tomas.becomebasketballpro.Fragments;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.ArticleDetailsActivity;
import com.example.tomas.becomebasketballpro.DBHandler.ArticleDbHandler;
import com.example.tomas.becomebasketballpro.DBHandler.MotivationDbHandler;
import com.example.tomas.becomebasketballpro.DBHandler.SuccessDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.Model.MotivationModel;
import com.example.tomas.becomebasketballpro.Model.SuccessModel;
import com.example.tomas.becomebasketballpro.MotivationDetailsActivity;
import com.example.tomas.becomebasketballpro.R;
import com.example.tomas.becomebasketballpro.SuccessDetailsActivity;
import com.example.tomas.becomebasketballpro.ui.DynamicHeightNetworkImageView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
public class SuccessListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // @BindView(R.layout.fragment_article_list)
    Context context;

    View mRootView;
    ListView mListView;
    private String URL_TO_HIT = "https://raw.githubusercontent.com/tomasmaks/Basketball-training-app/master/app/json/SuccessStories.json";
    private ProgressDialog dialog;
    SuccessAdapter adapter;
    private SwipeRefreshLayout refreshLayout = null;
    SuccessDbHandler dbHandler;
    List<SuccessModel> result = null;


    public static SuccessListFragment newInstance(int sectionNumber) {
        SuccessListFragment fragment = new SuccessListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public SuccessListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
//        // Create default options which will be used for every
//        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

    }

    public void LoadSuccess() {

        new FetchSuccessTask().execute(URL_TO_HIT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            LoadSuccess();

        }
        dbHandler = new SuccessDbHandler(getActivity());
        mRootView  = inflater.inflate(R.layout.fragment_success_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.mListView);

        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.green));
        refreshLayout.setOnRefreshListener(this);

        NetworkUtils utils = new NetworkUtils(getActivity());
        if(!utils.isConnectingToInternet() && savedInstanceState == null) {
            result = dbHandler.getAllSuccess();
            adapter = new SuccessAdapter(getActivity().getApplicationContext(),R.layout.fragment_article_list_items, result);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                    SuccessModel successModel = result.get(position2);
                    Intent intent = new Intent(getActivity(), SuccessDetailsActivity.class);
                    intent.putExtra("successModel", new Gson().toJson(successModel));
                    getActivity().startActivity(intent);
                }
            });


        }

        return mRootView;
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
                    new FetchSuccessTask().execute(URL_TO_HIT);
                    Toast.makeText(getActivity().getApplicationContext(), "Refresh success", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    }


    public class FetchSuccessTask extends AsyncTask<String,String, List<SuccessModel>> {

        private final String LOG_TAG = FetchSuccessTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<SuccessModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("SuccessStories");

                List<SuccessModel> successModelList = new ArrayList<>();

                Gson gson = new Gson();

                dbHandler.deleteTable();

                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself which is commented below
                     */
                    SuccessModel successModel = gson.fromJson(finalObject.toString(), SuccessModel.class);
                    successModel.setThumbnail(finalObject.getString("thumb"));
                    successModel.setTitle(finalObject.getString("title"));
                    successModel.setBody(finalObject.getString("body"));
                    successModel.setImage(finalObject.getString("photo"));
                    successModel.setData(finalObject.getString("published_date"));

                    successModelList.add(successModel);
                    dbHandler.addSuccess(successModel);
                }
                return successModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }


        @Override
        protected void onPostExecute(final List<SuccessModel> result) {
            super.onPostExecute(result);

            dialog.dismiss();


            if(result != null) {
                adapter = new SuccessAdapter(getActivity().getApplicationContext(), R.layout.fragment_success_list_items, result);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SuccessModel successModel = result.get(position);
                        Intent intent = new Intent(getActivity(), SuccessDetailsActivity.class);
                        intent.putExtra("successModel", new Gson().toJson(successModel));
                        getActivity().startActivity(intent);

                    }
                });
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class SuccessAdapter extends ArrayAdapter {

        private List<SuccessModel> successModelList;
        private int resource;
        private LayoutInflater inflater;
        public SuccessAdapter(Context context, int resource, List<SuccessModel> objects) {
            super(context, resource, objects);
            successModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
                holder.articleTitle = (TextView)convertView.findViewById(R.id.article_title);
                holder.articleData = (TextView)convertView.findViewById(R.id.article_data);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(successModelList.get(position).getThumbnail(), holder.thumbnail);
            // ImageLoader.getInstance().displayImage(articleModelList.get(position).getPhoto(), holder.Photo);

            holder.articleTitle.setText(successModelList.get(position).getTitle());
            holder.articleData.setText("Added on: " + successModelList.get(position).getData());

            return convertView;
        }


        class ViewHolder{
            private ImageView thumbnail;
            private TextView articleTitle;
            private TextView articleData;
        }

    }

}