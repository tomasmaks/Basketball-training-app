package com.example.tomas.becomebasketballpro.Fragments;

import android.app.Activity;
import android.content.Context;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.Helpers.NetworkUtils;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
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
 * Created by Tomas on 11/08/2016.
 */
public class BallTrainingFragment extends ListFragment {
    View mRootView;
    GridView mGridview;
    ListAdapter adapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    List<BallTrainingModel> ballTrainingModel = new ArrayList<>();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getActivity());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        mRootView = inflater.inflate(R.layout.fragment_balltraining, container, false);

        mGridview = (GridView) mRootView.findViewById(R.id.list);

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/").child("basketball");

        mReference.addValueEventListener(new ValueEventListener() {

            /*
             * onDataChange method to read a static snapshot of the contents at given JSON object
             * This method is triggered once when the listener is attached
             * and again every time the data changes.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ballTrainingModel = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ballTrainingModel.add(postSnapshot.getValue(BallTrainingModel.class));
                }

                adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.fragment_balltraining_content, ballTrainingModel);

                mGridview.setAdapter(adapter);
                mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        BallTrainingSecondFragment ballTrainingSecondFragment = new BallTrainingSecondFragment();
                        Bundle bundle = new Bundle();
                        int postKey = ballTrainingModel.get(position).getIds();
                        bundle.putInt(BallTrainingSecondFragment.EXTRA_POST_KEY, postKey);
                        bundle.putLong(FirebaseAnalytics.Param.ITEM_ID, id);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                        ballTrainingSecondFragment.setArguments(bundle);
                        ((MainActivity) getActivity()).switchFragment(ballTrainingSecondFragment, false);

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
        if (!utils.isConnectingToInternet() && savedInstanceState == null) {
            Toast.makeText(getActivity().getApplicationContext(), "No internet connection... Please connect to load posts",
                    Toast.LENGTH_SHORT).show();
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public static BallTrainingFragment newInstance(int sectionNumber) {
        BallTrainingFragment fragment = new BallTrainingFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BallTrainingFragment() {
        // Required empty public constructor
    }

    public class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        Context context;
        private List<BallTrainingModel> ballTrainingModelList;
        int resource;

        public ListAdapter(Context context, int resource, List<BallTrainingModel> ballTrainingModelList) {
            this.context = context;
            this.resource = resource;
            this.ballTrainingModelList = ballTrainingModelList;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        class ViewHolder {
            private TextView category;
            private ImageView tagThumb;
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
                view = inflater.inflate(R.layout.fragment_balltraining_content, parent, false);
                mViewHolder.category = (TextView) view.findViewById(R.id.category_name);
                mViewHolder.tagThumb = (ImageView) view.findViewById(R.id.thumb);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }

            Picasso.with(getActivity()).load(ballTrainingModelList.get(position).getCatThumb()).into(mViewHolder.tagThumb);

            mViewHolder.category.setText(ballTrainingModelList.get(position).getCategory());

            return view;
        }
    }
}




