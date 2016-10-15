package com.example.tomas.becomebasketballpro.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Model.MotivationModel;
import com.example.tomas.becomebasketballpro.MotivationDetailsActivity;
import com.example.tomas.becomebasketballpro.R;

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

    View mRootView;
    ListView mListView;
    MotivationAdapter adapter;
    List<MotivationModel> motivationModel = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    private FirebaseAnalytics mFirebaseAnalytics;

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

        mRootView = inflater.inflate(R.layout.fragment_motivation_list, container, false);

        mListView = (ListView) mRootView.findViewById(R.id.mListView);

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/").child("motivation");

        mReference.addValueEventListener(new ValueEventListener() {

            /*
             * onDataChange method to read a static snapshot of the contents at given JSON object
             * This method is triggered once when the listener is attached
             * and again every time the data changes.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                motivationModel = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    motivationModel.add(postSnapshot.getValue(MotivationModel.class));

                }

                adapter = new MotivationAdapter(getActivity().getApplicationContext(), R.layout.fragment_motivation_list_items, motivationModel);

                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Intent intent = new Intent(getActivity(), MotivationDetailsActivity.class);
                        String postKey = motivationModel.get(position).getId();
                        intent.putExtra(MotivationDetailsActivity.EXTRA_POST_KEY, postKey);
                        Bundle bundle = new Bundle();
                        bundle.putLong(FirebaseAnalytics.Param.ITEM_ID, id);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                        getActivity().startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.log(databaseError.toString());
            }
        });


        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Firebase.setAndroidContext(getActivity());
    }

    public class MotivationAdapter extends ArrayAdapter {

        private List<MotivationModel> motivationModelList;
        private int resource;
        private LayoutInflater inflater;

        public MotivationAdapter(Context context, int resource, List<MotivationModel> objects) {
            super(context, resource, objects);
            motivationModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(getActivity()).load(motivationModelList.get(position).getThumb()).into(holder.thumbnail);

            return convertView;
        }


        class ViewHolder {
            private ImageView thumbnail;
        }

    }
}
