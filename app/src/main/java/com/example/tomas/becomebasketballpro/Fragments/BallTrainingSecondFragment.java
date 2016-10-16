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
    View mRootView;
    ListAdapter adapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    List<BallTrainingModel> ballTrainingModel = new ArrayList<>();
    ListView mListView;

    public static final String EXTRA_POST_KEY = "post_key";

    private AdView mAdView;

    int mPostKey;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        mRootView = inflater.inflate(R.layout.fragment_balltraining_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.listView);

        mAdView = (AdView) mRootView.findViewById(R.id.adView);

        mAdView.setAdListener(new ToastAdListener(getActivity()));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mPostKey = getArguments().getInt(EXTRA_POST_KEY);
//        if (mPostKey == 0) {
//            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
//        }

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/basketball/").child(String.valueOf(mPostKey)).child("exercises");

        mReference.addValueEventListener(new ValueEventListener() {

            /*
             * onDataChange method to read a static snapshot of the contents at given JSON object
             * This method is triggered once when the listener is attached
             * and again every time the data changes.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ballTrainingModel = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    ballTrainingModel.add(postSnapshot.getValue(BallTrainingModel.class));

                }

                adapter = new ListAdapter(getActivity().getApplicationContext(), ballTrainingModel);

                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Intent intent = new Intent(getActivity(), BallTrainingThirdActivity.class);
                        intent.putExtra(BallTrainingThirdActivity.EXTRA_POST_KEY, mPostKey);
                        int detailKey = ballTrainingModel.get(position).getId();
                        intent.putExtra(BallTrainingThirdActivity.EXTRA_DETAIL_KEY, detailKey);
                        Bundle bundle = new Bundle();
                        bundle.putLong(FirebaseAnalytics.Param.ITEM_ID, id);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                        getActivity().startActivity(intent);
                    }
                });
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


    public class ListAdapter extends BaseAdapter {
        Context context;
        private List<BallTrainingModel> ballTrainingModelList;

        public ListAdapter(Context context,List<BallTrainingModel> ballTrainingModelList){
            this.context = context;
            this.ballTrainingModelList = ballTrainingModelList;
        }

        class ViewHolder {

            private TextView ids;
            private TextView id;
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
            if(view == null){
                mViewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService
                        (Activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_balltraining_list_items, parent, false);
               // mViewHolder.ids = (TextView)view.findViewById(R.id.category_id);
               // mViewHolder.id = (TextView)view.findViewById(R.id.exercise_id);
                mViewHolder.name = (TextView)view.findViewById(R.id.name);
                mViewHolder.description = (TextView)view.findViewById(R.id.description);
                mViewHolder.thumb=(ImageView)view.findViewById(R.id.thumb_image);
                view.setTag(mViewHolder);
            }
            else {
                mViewHolder = (ViewHolder) view.getTag();
            }

            Picasso.with(getActivity()).load(ballTrainingModelList.get(position).getThumb()).into(mViewHolder.thumb);

            mViewHolder.name.setText(ballTrainingModelList.get(position).getName());
            mViewHolder.description.setText(ballTrainingModelList.get(position).getDescription());
           // mViewHolder.ids.setText(ballTrainingModelList.get(position).getIds());
           // mViewHolder.id.setText(ballTrainingModelList.get(position).getId());

            return view;
        }

    }
}