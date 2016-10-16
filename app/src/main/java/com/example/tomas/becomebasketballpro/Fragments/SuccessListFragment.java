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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.ArticleDetailsActivity;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.Model.SuccessModel;
import com.example.tomas.becomebasketballpro.R;
import com.example.tomas.becomebasketballpro.SuccessDetailsActivity;
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
public class SuccessListFragment extends Fragment {

    View mRootView;
    SuccessAdapter adapter;
    List<SuccessModel> successModel = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ListView mListView;

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

        mListView = (ListView) mRootView.findViewById(R.id.mListView);

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

                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Intent intent = new Intent(getActivity(), SuccessDetailsActivity.class);
                        String postKey = successModel.get(position).getId();
                        intent.putExtra(SuccessDetailsActivity.EXTRA_SUCCESS_KEY, postKey);
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

    public class SuccessAdapter extends ArrayAdapter {

        private List<SuccessModel> successModelList;
        private int resource;
        private LayoutInflater inflater;
        public SuccessAdapter(Context context, int resource, List<SuccessModel> objects) {
            super(context, resource, objects);
            successModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
                holder.articleTitle = (TextView)convertView.findViewById(R.id.article_title);
                holder.articleData = (TextView)convertView.findViewById(R.id.article_data);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(getActivity()).load(successModelList.get(position).getThumb()).into(holder.thumbnail);

            holder.articleTitle.setText(successModelList.get(position).getTitle());
            holder.articleData.setText("Added on: " + successModelList.get(position).getPublished_date());

            return convertView;
        }


        class ViewHolder{
            private ImageView thumbnail;
            private TextView articleTitle;
            private TextView articleData;
        }

    }

}