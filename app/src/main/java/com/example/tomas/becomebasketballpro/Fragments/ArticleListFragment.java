package com.example.tomas.becomebasketballpro.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.utils.RecyclerItemClickListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

import java.util.ArrayList;
import java.util.List;


public class ArticleListFragment extends Fragment {

    View mRootView;
    ArticleAdapter adapter;
    List<ArticleModel> articleModel = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    RecyclerView mRecyclerView;

    private FirebaseAnalytics mFirebaseAnalytics;

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        mRootView = inflater.inflate(R.layout.fragment_article_list, container, false);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/").child("article");

        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                articleModel = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    articleModel.add(postSnapshot.getValue(ArticleModel.class));

                }
                adapter = new ArticleAdapter(getActivity().getApplicationContext(), R.layout.fragment_article_list_items, articleModel);

                mRecyclerView.setAdapter(adapter);
                mRecyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                                String postKey = articleModel.get(position).getId();
                                intent.putExtra(ArticleDetailsActivity.EXTRA_POST_KEY, postKey);
                                getActivity().startActivity(intent);

                            }
                            @Override public void onLongItemClick(View view, int position) {
                                // do whatever
                            }

                        })
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.log(databaseError.toString());

            }
        });

        NetworkUtils utils = new NetworkUtils(getActivity());
        if(!utils.isConnectingToInternet() && savedInstanceState == null) {
            Toast.makeText(getActivity().getApplicationContext(), "No internet connection... Please connect to load posts",
                    Toast.LENGTH_SHORT).show();
        }

        return mRootView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView articleTitle;
        private TextView articleData;

        public ViewHolder(View view) {
            super(view);
            //getting XML object
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            articleTitle = (TextView) view.findViewById(R.id.article_title);
            articleData = (TextView) view.findViewById(R.id.article_data);
        }
    }

    public class ArticleAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final String TAG = "ArticleAdapter";

        private List<ArticleModel> articleModelList;
        private int resource;
        private LayoutInflater inflater;
        private Context context;

        public ArticleAdapter(Context context, int resource, List<ArticleModel> objects) {
            this.articleModelList = objects;
            this.resource = resource;
            this.context = context;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_article_list_items, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            ArticleModel articleModel = articleModelList.get(position);
            Picasso.with(getActivity()).load(articleModelList.get(position).getThumb()).into(holder.thumbnail);

            holder.articleTitle.setText(articleModelList.get(position).getTitle());
            holder.articleData.setText("Added on: " + articleModelList.get(position).getPublished_date());
        }

        @Override
        public int getItemCount()
        {
            return articleModelList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}

