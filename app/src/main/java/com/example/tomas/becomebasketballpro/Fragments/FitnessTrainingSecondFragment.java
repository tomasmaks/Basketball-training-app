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

import com.example.tomas.becomebasketballpro.BallTrainingThirdActivity;
import com.example.tomas.becomebasketballpro.FitnessTrainingThirdActivity;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;
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
 * Created by Tomas on 05/09/2016.
 */
public class FitnessTrainingSecondFragment extends ListFragment {View mRootView;

    ListView mListView;
    ListAdapter adapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    List<FitnessTrainingModel> fitnessTrainingModel = new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        mRootView = inflater.inflate(R.layout.fragment_fitnesstraining_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.listView);

        mPostKey = getArguments().getInt(EXTRA_POST_KEY);

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/fitness/").child(String.valueOf(mPostKey)).child("exercises");

        mReference.addValueEventListener(new ValueEventListener() {

            /*
             * onDataChange method to read a static snapshot of the contents at given JSON object
             * This method is triggered once when the listener is attached
             * and again every time the data changes.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fitnessTrainingModel = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    fitnessTrainingModel.add(postSnapshot.getValue(FitnessTrainingModel.class));
                }

                adapter = new ListAdapter(getActivity().getApplicationContext(), fitnessTrainingModel);

                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Intent intent = new Intent(getActivity(), FitnessTrainingThirdActivity.class);
                        intent.putExtra(FitnessTrainingThirdActivity.EXTRA_POST_KEY, mPostKey);
                        int detailKey = fitnessTrainingModel.get(position).getId();
                        intent.putExtra(FitnessTrainingThirdActivity.EXTRA_DETAIL_KEY, detailKey);
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

        mAdView = (AdView) mRootView.findViewById(R.id.adView);
        mAdView.setAdListener(new ToastAdListener(getActivity()));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return mRootView;
    }

    public class ListAdapter extends BaseAdapter {
        Context context;
        private List<FitnessTrainingModel> fitnessTrainingModelList;

        public ListAdapter(Context context,List<FitnessTrainingModel> fitnessTrainingModelList){
            this.context = context;
            this.fitnessTrainingModelList = fitnessTrainingModelList;
        }

        class ViewHolder {
            private TextView name;
            private TextView description;
            private ImageView thumb;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fitnessTrainingModelList.size();
        }

        @Override
        public Object getItem(int pos) {
            // TODO Auto-generated method stub
            return fitnessTrainingModelList.get(pos);
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
                view = inflater.inflate(R.layout.fragment_fitnesstraining_list_items, parent, false);
                mViewHolder.name = (TextView)view.findViewById(R.id.name);
                mViewHolder.description = (TextView)view.findViewById(R.id.description);
                mViewHolder.thumb=(ImageView)view.findViewById(R.id.thumb_image);
                view.setTag(mViewHolder);
            }
            else {
                mViewHolder = (ViewHolder) view.getTag();
            }
            Picasso.with(getActivity()).load(fitnessTrainingModelList.get(position).getThumb()).into(mViewHolder.thumb);

            mViewHolder.name.setText(fitnessTrainingModelList.get(position).getName());
            mViewHolder.description.setText(fitnessTrainingModelList.get(position).getDescription());

            return view;
        }
    }
}