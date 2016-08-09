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


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment {


    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private View root;

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static NewsListFragment newInstance(int sectionNumber) {
        NewsListFragment fragment = new NewsListFragment();
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

//        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
//                .getDisplayMetrics());
//        pager.setPageMargin(pageMargin);

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tab_list, container, false);

        // Initialize the ViewPager and set an adapter
        pager = (ViewPager) root.findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        return root;
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
                    fragment = new ProductListFragment();
                    break;
                case 1:
                    fragment = new ProductListFragment();
                    break;
                case 2:
                    fragment = new ProductListFragment();
                    break;
                case 3:
                    fragment = new ArticleListFragment();
                    break;
                default:
                    break;
            }

            return fragment;
        }
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position2) {
            if (position2 == 0) {
                return "FIRST FRAG";
            } else if (position2 == 1) {
                return "SECOND FRAG";
            } else if (position2 == 2) {
                return "THIRD FRAG";
            } else if (position2 == 3) {
                return "FOURTH FRAG";
            } else {
                return super.getPageTitle(position2);
            }
        }




    }
}
