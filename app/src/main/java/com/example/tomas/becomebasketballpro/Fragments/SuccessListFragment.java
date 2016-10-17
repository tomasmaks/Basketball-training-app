package com.example.tomas.becomebasketballpro.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.ArticleDetailsActivity;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.SuccessModel;
import com.example.tomas.becomebasketballpro.R;
import com.example.tomas.becomebasketballpro.SuccessDetailsActivity;
import com.example.tomas.becomebasketballpro.utils.RecyclerItemClickListener;
import com.firebase.client.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SuccessListFragment extends Fragment {

    View mRootView;
    SuccessAdapter adapter;
    List<SuccessModel> successModel = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    RecyclerView mRecyclerView;

    private FirebaseAnalytics mFirebaseAnalytics;

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
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Firebase.setAndroidContext(getActivity());

        mRootView = inflater.inflate(R.layout.fragment_success_list, container, false);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/").child("success");

        mReference.addValueEventListener(new ValueEventListener() {

            /*
             * onDataChange method to read a static snapshot of the contents at given JSON object
             * This method is triggered once when the listener is attached
             * and again every time the data changes.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                successModel = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    successModel.add(postSnapshot.getValue(SuccessModel.class));

                }

                adapter = new SuccessAdapter(getActivity().getApplicationContext(), R.layout.fragment_success_list_items, successModel);

                mRecyclerView.setAdapter(adapter);
                mRecyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), SuccessDetailsActivity.class);
                                String postKey = successModel.get(position).getId();
                                intent.putExtra(SuccessDetailsActivity.EXTRA_SUCCESS_KEY, postKey);
                                getActivity().startActivity(intent);

                            }
                            @Override public void onLongItemClick(View view, int position) {
                                // do whatever
                            }

                        })
                );
            }

            //this will called when error occur while getting data from firebase
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

    public class SuccessAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final String TAG = "SuccesAdapter";

        private List<SuccessModel> successModelList;
        private int resource;
        private LayoutInflater inflater;
        private Context context;

        public SuccessAdapter(Context context, int resource, List<SuccessModel> objects) {
            this.successModelList = objects;
            this.resource = resource;
            this.context = context;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_success_list_items, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SuccessModel successModel = successModelList.get(position);
            Picasso.with(getActivity()).load(successModelList.get(position).getThumb()).into(holder.thumbnail);

            holder.articleTitle.setText(successModelList.get(position).getTitle());
            holder.articleData.setText("Added on: " + successModelList.get(position).getPublished_date());
        }

        @Override
        public int getItemCount() {
            return successModelList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}