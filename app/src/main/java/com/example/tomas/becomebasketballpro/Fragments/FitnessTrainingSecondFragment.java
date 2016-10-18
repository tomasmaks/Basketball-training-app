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

import com.example.tomas.becomebasketballpro.FitnessTrainingThirdActivity;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
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
public class FitnessTrainingSecondFragment extends ListFragment {
    View rootView;

    ListView listView;
    ListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<FitnessTrainingModel> fitnessTrainingModel = new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        rootView = inflater.inflate(R.layout.fragment_fitnesstraining_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_view);

        adView = (AdView) rootView.findViewById(R.id.ad_view);
        adView.setAdListener(new ToastAdListener(getActivity()));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        postKey = getArguments().getInt(EXTRA_POST_KEY);

        database = FirebaseDatabase.getInstance();

        reference = database.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/fitness/").child(String.valueOf(postKey)).child("exercises");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fitnessTrainingModel = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    fitnessTrainingModel.add(postSnapshot.getValue(FitnessTrainingModel.class));
                }

                adapter = new ListAdapter(getActivity().getApplicationContext(), fitnessTrainingModel);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Intent intent = new Intent(getActivity(), FitnessTrainingThirdActivity.class);
                        intent.putExtra(FitnessTrainingThirdActivity.EXTRA_POST_KEY, postKey);
                        int detailKey = fitnessTrainingModel.get(position).getId();
                        intent.putExtra(FitnessTrainingThirdActivity.EXTRA_DETAIL_KEY, detailKey);
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
        private List<FitnessTrainingModel> fitnessTrainingModelList;

        public ListAdapter(Context context, List<FitnessTrainingModel> fitnessTrainingModelList) {
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
            if (view == null) {
                mViewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService
                        (Activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_fitnesstraining_list_items, parent, false);
                mViewHolder.name = (TextView) view.findViewById(R.id.text_name);
                mViewHolder.description = (TextView) view.findViewById(R.id.text_description);
                mViewHolder.thumb = (ImageView) view.findViewById(R.id.image_thumb);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }
            Picasso.with(getActivity()).load(fitnessTrainingModelList.get(position).getThumb()).into(mViewHolder.thumb);

            mViewHolder.name.setText(fitnessTrainingModelList.get(position).getName());
            mViewHolder.description.setText(fitnessTrainingModelList.get(position).getDescription());

            return view;
        }
    }
}