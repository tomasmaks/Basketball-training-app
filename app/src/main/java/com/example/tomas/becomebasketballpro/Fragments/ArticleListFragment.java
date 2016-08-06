package com.example.tomas.becomebasketballpro.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.R;

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

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



   // @BindView(R.layout.fragment_article_list)
    Context context;

    View mRootView;
    ListView mListView;
    private String URL_TO_HIT = "https://gist.githubusercontent.com/tomasmaks/c1bb4dc91ae7972bd93e73b3ee632052/raw/87cadf10d349f19eca3b098e2552122c0140ef90/article.json";
    private ProgressDialog dialog;
    MovieAdapter adapter;
    private SwipeRefreshLayout refreshLayout = null;



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

//        int columnCount = getResources().getInteger(R.integer.grid_column_count);
//        StaggeredGridLayoutManager staggeredGridLayoutManager =
//        new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
//        mListView.setListManager(staggeredGridLayoutManager);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView  = inflater.inflate(R.layout.fragment_article_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.mListView);

        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.green));
        refreshLayout.setOnRefreshListener(this);

        return mRootView;



    }

//    private void setupRefreshLayout(View mRootView) {
//
//        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
//       mSwipeRefreshManager = new SwipeRefreshManager(mSwipeRefreshLayout, mRefreshHandler);
//
//    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(Constants.ARG_SECTION_NUMBER));
    }


    @Override
    public void onStart() {
        super.onStart();
        new FetchTask().execute(URL_TO_HIT);
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
                    Toast.makeText(getActivity().getApplicationContext(), "refresh success", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    }


    public class FetchTask extends AsyncTask<String,String, List<ArticleModel>> {

        private final String LOG_TAG = FetchTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
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
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("article");

                List<ArticleModel> articleModelList = new ArrayList<>();

                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself which is commented below
                     */
                    ArticleModel articleModel = gson.fromJson(finalObject.toString(), ArticleModel.class);
                    //articleModel.setThumbnail(finalObject.getString("thumb"));
                    articleModel.setTitle(finalObject.getString("title"));
//
//                    movieModel.setDuration(finalObject.getString("duration"));
//                    movieModel.setTagline(finalObject.getString("tagline"));
//                    movieModel.setImage(finalObject.getString("image"));
//                    movieModel.setStory(finalObject.getString("story"));
//
//                    List<ArticleModel.Cast> castList = new ArrayList<>();
//                    for(int j=0; j<finalObject.getJSONArray("cast").length(); j++){
//                        MovieModel.Cast cast = new MovieModel.Cast();
//                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
//                        castList.add(cast);
//                    }
//                    movieModel.setCastList(castList);
                    // adding the final object in the list
                    articleModelList.add(articleModel);
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

            dialog.dismiss();


              if(result != null) {
                adapter = new MovieAdapter(getActivity().getApplicationContext(), R.layout.fragment_article_list_items, result);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArticleModel articleModel = result.get(position);
                        ArticleDetailsFragment articleDetailsFragment = new ArticleDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("articleModel", new Gson().toJson(articleModel));
                        articleDetailsFragment.setArguments(bundle);
                        ((MainActivity) getActivity()).switchFragment(articleDetailsFragment, false);

                    }
                });
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MovieAdapter extends ArrayAdapter{

        private List<ArticleModel> articleModelList;
        private int resource;
        private LayoutInflater inflater;
        public MovieAdapter(Context context, int resource, List<ArticleModel> objects) {
            super(context, resource, objects);
            articleModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                //holder.thumbnail = (DynamicHeightNetworkImageView)convertView.findViewById(R.id.thumbnail);
                holder.articleTitle = (TextView)convertView.findViewById(R.id.article_title);
                // holder.articleSubtitle = (TextView)convertView.findViewById(R.id.article_subtitle);
                // holder.articleAuthor = (TextView)convertView.findViewById(R.id.article_author);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            // Then later, when you want to display image
            // ImageLoader.getInstance().displayImage(articleModelList.get(position).getThumbnail(), holder.thumbnail);

            holder.articleTitle.setText(articleModelList.get(position).getTitle());
            //holder.articleSubtitle.setText(articleModelList.get(position).getSubtitle());
            // holder.articleAuthor.setText(articleModelList.get(position).getAuthor());

            return convertView;
        }


        class ViewHolder{
            //private DynamicHeightNetworkImageView thumbnail;
            private TextView articleTitle;
            // private TextView articleSubtitle;
            // private TextView articleAuthor;
        }

    }

}



