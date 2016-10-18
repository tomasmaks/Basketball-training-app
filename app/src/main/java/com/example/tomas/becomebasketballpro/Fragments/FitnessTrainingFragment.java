package com.example.tomas.becomebasketballpro.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;
import com.example.tomas.becomebasketballpro.R;
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
public class FitnessTrainingFragment extends ListFragment {

    View rootView;
    GridView gridview;
    ListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<FitnessTrainingModel> fitnessTrainingModel = new ArrayList<>();

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        rootView = inflater.inflate(R.layout.fragment_fitnesstraining, container, false);

        gridview = (GridView) rootView.findViewById(R.id.grid_view);

        database = FirebaseDatabase.getInstance();

        reference = database.getReferenceFromUrl("https://basketball-training-app.firebaseio.com/").child("fitness");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fitnessTrainingModel = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    fitnessTrainingModel.add(postSnapshot.getValue(FitnessTrainingModel.class));
                }

                adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.fragment_fitnesstraining_content, fitnessTrainingModel);

                gridview.setAdapter(adapter);
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        FitnessTrainingSecondFragment fitnessTrainingSecondFragment = new FitnessTrainingSecondFragment();
                        Bundle bundle = new Bundle();
                        int postKey = fitnessTrainingModel.get(position).getIds();
                        bundle.putInt(FitnessTrainingSecondFragment.EXTRA_POST_KEY, postKey);
                        fitnessTrainingSecondFragment.setArguments(bundle);
                        bundle.putLong(FirebaseAnalytics.Param.ITEM_ID, id);
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                        ((MainActivity) getActivity()).switchFragment(fitnessTrainingSecondFragment, false);

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public static FitnessTrainingFragment newInstance(int sectionNumber) {
        FitnessTrainingFragment fragment = new FitnessTrainingFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FitnessTrainingFragment() {
        // Required empty public constructor
    }

    public class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        Context context;
        private List<FitnessTrainingModel> fitnessTrainingModelList;
        int resource;

        public ListAdapter(Context context, int resource, List<FitnessTrainingModel> fitnessTrainingModelList) {
            this.context = context;
            this.resource = resource;
            this.fitnessTrainingModelList = fitnessTrainingModelList;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        class ViewHolder {
            private TextView category;
            private ImageView tagThumb;
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
                view = inflater.inflate(R.layout.fragment_fitnesstraining_content, parent, false);

                mViewHolder.category = (TextView) view.findViewById(R.id.text_category_name);
                mViewHolder.tagThumb = (ImageView) view.findViewById(R.id.image_thumb);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }

            Picasso.with(getActivity()).load(fitnessTrainingModelList.get(position).getCatThumb()).into(mViewHolder.tagThumb);

            mViewHolder.category.setText(fitnessTrainingModelList.get(position).getCategory());

            return view;
        }
    }
}