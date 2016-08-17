package com.example.tomas.becomebasketballpro.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import com.example.tomas.becomebasketballpro.BallTrainingDetailsActivity;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Model.JSONParser;
import com.example.tomas.becomebasketballpro.Model.JSONParserString;
import com.example.tomas.becomebasketballpro.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
    // Creating JSON Parser object
    JSONParserString jsonParser = new JSONParserString();

    ArrayList<HashMap<String, String>> albumsList;

    // albums JSONArray
    JSONArray albums = null;

    // albums JSON url
    private static final String URL_ALBUMS = "https://gist.githubusercontent.com/tomasmaks/c9a92ab502f69514b923128cb1af0910/raw/5a30ca568a6c85cc2fd27a86b81c9ad09b87abf1/albums";

    // ALL JSON node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_SONGS_COUNT = "songs_count";

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

        // Hashmap for ListView
        albumsList = new ArrayList<HashMap<String, String>>();

        // Loading Albums JSON in Background Thread
        new LoadAlbums().execute();


        // get listview
        ListView lv = getListView();

        /**
         * Listview item click listener
         * TrackListActivity will be lauched by passing album id
         * */
        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                Intent i = new Intent(getActivity().getApplicationContext(), BallTrainingDetailsActivity.class);

                // send album id to tracklist activity to get list of songs under that album
                String album_id = ((TextView) view.findViewById(R.id.album_id)).getText().toString();
                i.putExtra("album_id", album_id);

                startActivity(i);
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
    class LoadAlbums extends AsyncTask<String, String, String> {

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
            String json = jsonParser.makeHttpRequest(URL_ALBUMS, "GET",
                    params);

            // Check your log cat for JSON reponse
            Log.d("Albums JSON: ", "> " + json);

            try {
                albums = new JSONArray(json);

                if (albums != null) {
                    // looping through All albums
                    for (int i = 0; i < albums.length(); i++) {
                        JSONObject c = albums.getJSONObject(i);

                        // Storing each json item values in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String songs_count = c.getString(TAG_SONGS_COUNT);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_SONGS_COUNT, songs_count);

                        // adding HashList to ArrayList
                        albumsList.add(map);
                    }
                } else {
                    Log.d("Albums: ", "null");
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
                            getActivity(), albumsList,
                            R.layout.fragment_balltraining_content, new String[]{TAG_ID,
                            TAG_NAME, TAG_SONGS_COUNT}, new int[]{
                            R.id.album_id, R.id.album_name, R.id.songs_count});

                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

}

//
//    View mRootView;
//    String URL_TO_HIT = "https://gist.githubusercontent.com/tomasmaks/afbf3e836dabd72c95c4b3ec90e291ed/raw/7ba41948865f78f1ccb4058c61f8c6e06c601300/BasketballTraining.json";
//    BallTrainingAdapter adapter;
//    RecyclerView mRecyclerView;
//    private List<BallTrainingModel> ballTraining;
//    GridLayoutManager mGridLayoutManager;
//
//    public BallTrainingFragment() {
//        // Required empty public constructor
//    }
//
//    public static BallTrainingFragment newInstance(int sectionNumber) {
//        BallTrainingFragment fragment = new BallTrainingFragment();
//        Bundle args = new Bundle();
//        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Create default options which will be used for every
//        //  displayImage(...) call if no options will be passed to this method
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
//                .defaultDisplayImageOptions(defaultOptions)
//                .build();
//        ImageLoader.getInstance().init(config); // Do it on Application start
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//
//        // Inflate the layout for this fragment
//        mRootView = inflater.inflate(R.layout.fragment_balltraining, container, false);
//
//        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.balltraining_list);
//
//        mRecyclerView.setHasFixedSize(true);
//
//        // Display under one column
//        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        mRecyclerView.setLayoutManager(mGridLayoutManager);
//
//        // Set orientation
//        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mGridLayoutManager);
//
//        return mRootView;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        new FetchBallTask().execute(URL_TO_HIT);
//    }
//
//public class FetchBallTask extends AsyncTask<String, String, List<BallTrainingModel>> {
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//
//    @Override
//    protected List<BallTrainingModel> doInBackground(String... params) {
//        HttpURLConnection connection = null;
//        BufferedReader reader = null;
//
//        try {
//            URL url = new URL(params[0]);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//            InputStream stream = connection.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(stream));
//            StringBuilder buffer = new StringBuilder();
//            String line ="";
//            while ((line = reader.readLine()) != null){
//                buffer.append(line);
//            }
//
//            String finalJson = buffer.toString();
//
//            JSONObject parentObject = new JSONObject(finalJson);
//            JSONArray parentArray = parentObject.optJSONArray("BasketballTraining");
//
//            List<BallTrainingModel> ballTrainingModelList = new ArrayList<>();
//
//            Gson gson = new Gson();
//            for(int i=0; i<parentArray.length(); i++) {
//                JSONObject finalObject = parentArray.getJSONObject(i);
//                /**
//                 * below single line of code from Gson saves you from writing the json parsing yourself which is commented below
//                 */
//                BallTrainingModel ballTrainingModel = gson.fromJson(finalObject.toString(), BallTrainingModel.class);
//                ballTrainingModel.setCategory(finalObject.getString("category"));
//                ballTrainingModel.setThumbnail(finalObject.getString("thumb"));
//
//                List<BallTrainingModel.Cast> castList = new ArrayList<>();
//                for(int j=0; j<finalObject.getJSONArray("cast").length(); j++){
//                    BallTrainingModel.Cast cast = new BallTrainingModel.Cast();
//                    cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
//                    castList.add(cast);
//                }
//                ballTrainingModel.setCastList(castList);
//                // adding the final object in the list
//                ballTrainingModelList.add(ballTrainingModel);
//            }
//
//            return ballTrainingModelList;
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally {
//            if(connection != null) {
//                connection.disconnect();
//            }
//            try {
//                if(reader != null) {
//                    reader.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//
//    @Override
//    protected void onPostExecute(final List<BallTrainingModel> result) {
//        super.onPostExecute(result);
//
//        if(result != null) {
//            adapter = new BallTrainingAdapter(getActivity().getApplicationContext(), R.layout.fragment_balltraining_content, result);
//            mRecyclerView.setAdapter(adapter);
//            mRecyclerView.setOnClickListener(new AdapterView.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = mRecyclerView.indexOfChild(v);
////                        Intent intent = new Intent(getActivity(), BallTrainingDetailsActivity.class);
////                        Bundle bundle = new Bundle();
////                        intent.putExtras(bundle);
////                        bundle.putString("article-id", result.get(position).getArticleId());
////                        bundle.putString("article-title", result.get(position).getArticleTitle());
////                        bundle.putString("article-image-url", result.get(position).getImageUrl());
//                    BallTrainingModel ballTrainingModel = result.get(position);
//                    Intent intent = new Intent(getActivity(), BallTrainingDetailsActivity.class);
//                    intent.putExtra("ballTrainingModel", new Gson().toJson(ballTrainingModel));
//                    getActivity().startActivity(intent);
//                }
//            });
//        } else {
//            Toast.makeText(getActivity().getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
//
//public class BallTrainingAdapter extends RecyclerView.Adapter<CustomViewHolder> {
//    private List<BallTrainingModel> ballTrainingList;
//    private int resource;
//    private LayoutInflater inflater;
//
//    public BallTrainingAdapter(Context context,int resource, List<BallTrainingModel> objects) {
//        ballTrainingList = objects;
//        this.resource = resource;
//        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_balltraining_content, null);
//
//        CustomViewHolder viewHolder = new CustomViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {
//        BallTrainingModel ballTraining = ballTrainingList.get(position);
//
////            //Download image using picasso library
////            Picasso.with(mContext).load(ballTrainingModel.getThumbnail())
////                    .error(R.drawable.placeholder)
////                    .placeholder(R.drawable.placeholder)
////                    .into(customViewHolder.imageView);
//
//        ImageLoader.getInstance().displayImage(ballTrainingList.get(position).getThumbnail(), customViewHolder.thumbnail);
//
//        //Setting text view title
////            customViewHolder.textView.setText(ballTrainingList.get(position).getCategory());
//    }
//
//    @Override
//    public int getItemCount() {
//        return (null != ballTrainingList ? ballTrainingList.size() : 0);
//    }
//
//}
//
//public class CustomViewHolder extends RecyclerView.ViewHolder{
//
//    //        protected TextView textView;
//    protected ImageView thumbnail;
//
//    public CustomViewHolder(View view) {
//        super(view);
////            this.textView = (TextView) view.findViewById(R.id.category);
//        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//    }
//}

