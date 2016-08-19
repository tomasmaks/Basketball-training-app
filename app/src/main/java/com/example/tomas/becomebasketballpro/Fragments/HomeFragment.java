package com.example.tomas.becomebasketballpro.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.MainActivity;
import com.example.tomas.becomebasketballpro.R;

/**
 * Created by Tomas on 10/08/2016.
 */
public class HomeFragment extends Fragment {


    Context context;

    View mRootView;
   // ListView mListView;
    private ProgressDialog dialog;
    private RelativeLayout btn_lay;
    private RelativeLayout btn_lay2;
    private RelativeLayout btn_lay3;

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

        mRootView  = inflater.inflate(R.layout.fragment_home, container, false);

        btn_lay = (RelativeLayout) mRootView.findViewById(R.id.btn_lay);

        btn_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
                newsFragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(newsFragment, false);
            }
        });

        btn_lay2 = (RelativeLayout) mRootView.findViewById(R.id.btn_lay2);

        btn_lay2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
                newsFragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(newsFragment, false);
            }
        });

        btn_lay3 = (RelativeLayout) mRootView.findViewById(R.id.btn_lay3);

        btn_lay3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
                newsFragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(newsFragment, false);
            }
        });


        return mRootView;
    }

}