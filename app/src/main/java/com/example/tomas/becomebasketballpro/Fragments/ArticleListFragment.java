package com.example.tomas.becomebasketballpro.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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



import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ArticleListFragment extends Fragment {

    View mRootView;
    ArticleAdapter adapter;
    private SwipeRefreshLayout refreshLayout = null;
   // ArticleDbHandler dbHandler;
   List<ArticleModel> articleModel = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ListView mListView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Firebase.setAndroidContext(getActivity());

        // dbHandler = new ArticleDbHandler(getActivity());
        mRootView = inflater.inflate(R.layout.fragment_article_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.mListView);


//        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
//        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.green));
//        refreshLayout.setOnRefreshListener(this);

//        NetworkUtils utils = new NetworkUtils(getActivity());
//        if(!utils.isConnectingToInternet() && savedInstanceState == null) {
//
//            result = dbHandler.getAllArticle();
//            adapter = new ArticleAdapter(getActivity().getApplicationContext(),R.layout.fragment_article_list_items, result);
//            mListView.setAdapter(adapter);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
//                    ArticleModel articleModel = result.get(position2);
//                    Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
//                    intent.putExtra("articleModel", new Gson().toJson(articleModel));
//                    getActivity().startActivity(intent);
//                }
//            });
//
//        }


        mDatabase = FirebaseDatabase.getInstance();

       // mReference = mDatabase.getReference().child("article");
        mReference = mDatabase.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/article");
        //set value event listener for firebase reference
        mReference.addValueEventListener(new ValueEventListener() {

            /*
             * onDataChange method to read a static snapshot of the contents at given JSON object
             * This method is triggered once when the listener is attached
             * and again every time the data changes.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<ArticleModel>> type = new GenericTypeIndicator<List<ArticleModel>>() {
                };
                articleModel = dataSnapshot.getValue(type);
               // adapter.notifyDataSetChanged();


                adapter = new ArticleAdapter(getActivity().getApplicationContext(), R.layout.fragment_article_list_items, articleModel);

                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        final DatabaseReference articleRef = mDatabase.getRef(position);
                        Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                        final String postKey = mReference.getKey();
                        intent.putExtra(ArticleDetailsActivity.EXTRA_POST_KEY, postKey);
                        getActivity().startActivity(intent);

                    }
                });

            }

            //this will called when error occur while getting data from firebase
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });



        return mRootView;
    }



    public View getJsonString(ArticleModel model) {

    return null;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Firebase.setAndroidContext(getActivity());

    }




//    @Override
//    public void onRefresh() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//             //  handler.sendEmptyMessage(0);
//            }
//        }).start();
//
//
//    }


    public class ArticleAdapter extends ArrayAdapter {

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

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                holder.articleTitle = (TextView) convertView.findViewById(R.id.article_title);
                holder.articleData = (TextView) convertView.findViewById(R.id.article_data);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Picasso.with(getActivity()).load(articleModelList.get(position).getThumbnail()).into(holder.thumbnail);

            holder.articleTitle.setText(articleModelList.get(position).getTitle());
            holder.articleData.setText("Added on: " + articleModelList.get(position).getData());

            return convertView;
        }

        class ViewHolder {
            private ImageView thumbnail;
            private TextView articleTitle;
            private TextView articleData;
        }
    }
}