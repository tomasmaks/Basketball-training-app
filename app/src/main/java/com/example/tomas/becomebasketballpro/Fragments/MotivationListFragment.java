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
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.MotivationModel;
import com.example.tomas.becomebasketballpro.MotivationDetailsActivity;
import com.example.tomas.becomebasketballpro.R;

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

/**
 * Created by Tomas on 09/08/2016.
 */
public class MotivationListFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    MotivationAdapter adapter;
    List<MotivationModel> motivationModel = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;

    private FirebaseAnalytics firebaseAnalytics;

    public static MotivationListFragment newInstance(int sectionNumber) {
        MotivationListFragment fragment = new MotivationListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public MotivationListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Firebase.setAndroidContext(getActivity());

        rootView = inflater.inflate(R.layout.fragment_motivation_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        database = FirebaseDatabase.getInstance();

        reference = database.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/").child("motivation");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                motivationModel = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    motivationModel.add(postSnapshot.getValue(MotivationModel.class));

                }

                adapter = new MotivationAdapter(getActivity().getApplicationContext(), R.layout.fragment_motivation_list_items, motivationModel);

                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), MotivationDetailsActivity.class);
                                String postKey = motivationModel.get(position).getId();
                                intent.putExtra(MotivationDetailsActivity.EXTRA_POST_KEY, postKey);
                                getActivity().startActivity(intent);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
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
        if (!utils.isConnectingToInternet() && savedInstanceState == null) {
            Toast.makeText(getActivity().getApplicationContext(), "No internet connection... Please connect to load posts",
                    Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Firebase.setAndroidContext(getActivity());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            //getting XML object
            thumbnail = (ImageView) view.findViewById(R.id.image_thumbnail);
        }
    }

    public class MotivationAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final String TAG = "ArticleAdapter";

        private List<MotivationModel> motivationModelList;
        private int resource;
        private LayoutInflater inflater;
        private Context context;

        public MotivationAdapter(Context context, int resource, List<MotivationModel> objects) {
            this.motivationModelList = objects;
            this.resource = resource;
            this.context = context;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_motivation_list_items, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MotivationModel motivationModel = motivationModelList.get(position);
            Picasso.with(getActivity()).load(motivationModelList.get(position).getThumb()).into(holder.thumbnail);
        }

        @Override
        public int getItemCount() {
            return motivationModelList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}
