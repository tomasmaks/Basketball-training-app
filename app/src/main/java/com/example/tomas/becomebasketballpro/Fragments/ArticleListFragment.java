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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.ArticleDetailsActivity;
import com.example.tomas.becomebasketballpro.DBHandler.ArticleDbHandler;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.google.gson.Gson;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.R;
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


public class ArticleListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View mRootView;
    ListView mListView;
    private String URL_TO_HIT = "https://firebasestorage.googleapis.com/v0/b/basketball-training-app.appspot.com/o/article.json?alt=media&token=21471e3d-e54e-45cb-b3fd-d03bd97c81ba";
    ArticleAdapter adapter;
    private SwipeRefreshLayout refreshLayout = null;
    ArticleDbHandler dbHandler;
    List<ArticleModel> result = null;


    public static ArticleListFragment newInstance(int sectionNumber) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public ArticleListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void LoadArticle() {

        new FetchTask().execute(URL_TO_HIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            LoadArticle();

        }
        dbHandler = new ArticleDbHandler(getActivity());
        mRootView  = inflater.inflate(R.layout.fragment_article_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.mListView);


        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.green));
        refreshLayout.setOnRefreshListener(this);

        NetworkUtils utils = new NetworkUtils(getActivity());
        if(!utils.isConnectingToInternet() && savedInstanceState == null) {

            result = dbHandler.getAllArticle();
            adapter = new ArticleAdapter(getActivity().getApplicationContext(),R.layout.fragment_article_list_items, result);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                    ArticleModel articleModel = result.get(position2);
                    Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                    intent.putExtra("articleModel", new Gson().toJson(articleModel));
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
                    new FetchTask().execute(URL_TO_HIT);
                    refreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    }


    public class FetchTask extends AsyncTask<String,String, List<ArticleModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<ArticleModel> doInBackground(String... params) {
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
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("article");


                List<ArticleModel> articleModelList = new ArrayList<>();

                Gson gson = new Gson();

                dbHandler.deleteTable();

                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself which is commented below
                     */
                    ArticleModel articleModel = gson.fromJson(finalObject.toString(), ArticleModel.class);
                    articleModel.setThumbnail(finalObject.getString("thumb"));
                    articleModel.setTitle(finalObject.getString("title"));
                    articleModel.setBody(finalObject.getString("body"));
                    articleModel.setImage(finalObject.getString("photo"));
                    articleModel.setData(finalObject.getString("published_date"));
                    articleModel.setVideoURI(finalObject.getString("video"));


                    articleModelList.add(articleModel);
                    dbHandler.addArticle(articleModel);
                }


                return articleModelList;

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
        protected void onPostExecute(final List<ArticleModel> result) {
            super.onPostExecute(result);



            if(result != null) {
                adapter = new ArticleAdapter(getActivity().getApplicationContext(), R.layout.fragment_article_list_items, result);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                        ArticleModel articleModel = result.get(position2);
                        Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                        intent.putExtra("articleModel", new Gson().toJson(articleModel));
                        getActivity().startActivity(intent);

                    }
                });
            }else {
                Toast.makeText(getActivity().getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ArticleAdapter extends ArrayAdapter{

        private List<ArticleModel> articleModelList;
        private int resource;
        private LayoutInflater inflater;
        public ArticleAdapter(Context context, int resource, List<ArticleModel> objects) {
            super(context, resource, objects);
            articleModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return articleModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return articleModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
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
            Picasso.with(getActivity()).load(articleModelList.get(position).getThumbnail()).into(holder.thumbnail);

            holder.articleTitle.setText(articleModelList.get(position).getTitle());
            holder.articleData.setText("Added on: " + articleModelList.get(position).getData());

            return convertView;
        }


        class ViewHolder{
            private ImageView thumbnail;
            private TextView articleTitle;
            private TextView articleData;
        }

    }

}



