package com.example.tomas.becomebasketballpro.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.BallTrainingThirdActivity;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.R;
import com.example.tomas.becomebasketballpro.utils.ToastAdListener;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
 * Created by Tomas on 14/08/2016.
 */
public class BallTrainingSecondFragment extends ListFragment {
    View rootView;
    ListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<BallTrainingModel> ballTrainingModel = new ArrayList<>();
    ListView listView;

    public static final String EXTRA_POST_KEY = "post_key";

    private AdView adView;

    int postKey;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Firebase.setAndroidContext(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        rootView = inflater.inflate(R.layout.fragment_balltraining_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_view);

        adView = (AdView) rootView.findViewById(R.id.ad_view);

        adView.setAdListener(new ToastAdListener(getActivity()));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        postKey = getArguments().getInt(EXTRA_POST_KEY);

        database = FirebaseDatabase.getInstance();

        reference = database.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/basketball/").child(String.valueOf(postKey)).child("exercises");

        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ballTrainingModel = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ballTrainingModel.add(postSnapshot.getValue(BallTrainingModel.class));

                }

                adapter = new ListAdapter(getActivity().getApplicationContext(), ballTrainingModel);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Intent intent = new Intent(getActivity(), BallTrainingThirdActivity.class);
                        intent.putExtra(BallTrainingThirdActivity.EXTRA_POST_KEY, postKey);
                        int detailKey = ballTrainingModel.get(position).getId();
                        intent.putExtra(BallTrainingThirdActivity.EXTRA_DETAIL_KEY, detailKey);
                        Bundle bundle = new Bundle();
                        bundle.putLong(FirebaseAnalytics.Param.ITEM_ID, id);
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                        getActivity().startActivity(intent);
                    }
                });
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


    public class ListAdapter extends BaseAdapter {
        Context context;
        private List<BallTrainingModel> ballTrainingModelList;

        public ListAdapter(Context context, List<BallTrainingModel> ballTrainingModelList) {
            this.context = context;
            this.ballTrainingModelList = ballTrainingModelList;
        }

        class ViewHolder {
            private TextView name;
            private TextView description;
            private ImageView thumb;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ballTrainingModelList.size();
        }

        @Override
        public Object getItem(int pos) {
            // TODO Auto-generated method stub
            return ballTrainingModelList.get(pos);
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
                view = inflater.inflate(R.layout.fragment_balltraining_list_items, parent, false);

                mViewHolder.name = (TextView) view.findViewById(R.id.text_name);
                mViewHolder.description = (TextView) view.findViewById(R.id.text_description);
                mViewHolder.thumb = (ImageView) view.findViewById(R.id.image_thumb);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }

            Picasso.with(getActivity()).load(ballTrainingModelList.get(position).getThumb()).into(mViewHolder.thumb);

            mViewHolder.name.setText(ballTrainingModelList.get(position).getName());
            mViewHolder.description.setText(ballTrainingModelList.get(position).getDescription());

            return view;
        }

    }
}