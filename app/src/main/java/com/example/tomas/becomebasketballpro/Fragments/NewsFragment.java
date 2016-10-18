package com.example.tomas.becomebasketballpro.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.tomas.becomebasketballpro.Helpers.Constants;
import com.example.tomas.becomebasketballpro.R;
import com.example.tomas.becomebasketballpro.utils.ToastAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NewsFragment extends Fragment {


    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private View rootView;

    private AdView adView;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(int sectionNumber) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab_list, container, false);

        adView = (AdView) rootView.findViewById(R.id.ad_view);
        // Set the AdListener before building or loading the AdRequest.
        adView.setAdListener(new ToastAdListener(getActivity()));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Initialize the ViewPager and set an adapter
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        pagerSlidingTabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);

        return rootView;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position2) {
            Fragment fragment = null;
            switch (position2) {
                case 0:
                    fragment = new ArticleListFragment();
                    break;
                case 1:
                    fragment = new SuccessListFragment();
                    break;
                case 2:
                    fragment = new MotivationListFragment();
                    break;
                default:
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position2) {
            if (position2 == 0) {
                return "NEWS";
            } else if (position2 == 1) {
                return "SUCCESS STORIES";
            } else if (position2 == 2) {
                return "MOTIVATION";
            } else {
                return super.getPageTitle(position2);
            }
        }
    }
}
