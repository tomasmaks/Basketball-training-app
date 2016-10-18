package com.example.tomas.becomebasketballpro.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.R;
import com.example.tomas.becomebasketballpro.utils.ToastAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Tomas on 10/08/2016.
 */
public class HomeFragment extends Fragment {

    View rootView;
    private RelativeLayout btn_basketball_stories;
    private RelativeLayout btn_basketball_training;
    private RelativeLayout btn_fitness_training;

    private AdView adView;

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        adView = (AdView) rootView.findViewById(R.id.ad_view);

        adView.setAdListener(new ToastAdListener(getActivity()));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btn_basketball_stories = (RelativeLayout) rootView.findViewById(R.id.button_basketball_stories);

        btn_basketball_stories.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
                newsFragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(newsFragment, false);
            }
        });

        btn_basketball_training = (RelativeLayout) rootView.findViewById(R.id.button_basketball_training);

        btn_basketball_training.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BallTrainingFragment ballTrainingFragment = new BallTrainingFragment();
                Bundle bundle = new Bundle();
                ballTrainingFragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(ballTrainingFragment, false);
            }
        });

        btn_fitness_training = (RelativeLayout) rootView.findViewById(R.id.button_fitness_training);

        btn_fitness_training.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FitnessTrainingFragment newsFragment = new FitnessTrainingFragment();
                Bundle bundle = new Bundle();
                newsFragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(newsFragment, false);
            }
        });


        return rootView;
    }

}
