package com.example.tomas.becomebasketballpro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tomas.becomebasketballpro.Fragments.ArticleListFragment;
import com.example.tomas.becomebasketballpro.Fragments.BallTrainingFragment;
import com.example.tomas.becomebasketballpro.Fragments.FitnessTrainingFragment;
import com.example.tomas.becomebasketballpro.Fragments.HomeFragment;
import com.example.tomas.becomebasketballpro.Fragments.MotivationListFragment;
import com.example.tomas.becomebasketballpro.Fragments.NavigationDrawerFragment;
import com.example.tomas.becomebasketballpro.Fragments.NewsFragment;
import com.example.tomas.becomebasketballpro.Fragments.SuccessListFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment navigationDrawerFragment;

    private CharSequence title;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FirebaseCrash.log("Activity created");

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment mFragment = null;
        switch (position) {
            case 0:
                mFragment = HomeFragment.newInstance(0);
                break;
            case 1:
                mFragment = NewsFragment.newInstance(1);
                break;
            case 2:
                mFragment = BallTrainingFragment.newInstance(2);
                break;
            case 3:
                mFragment = FitnessTrainingFragment.newInstance(3);
                break;
            case 5:
                mFragment = SuccessListFragment.newInstance(4);
                break;
            case 6:
                mFragment = MotivationListFragment.newInstance(5);
                break;
            case 7:
                mFragment = ArticleListFragment.newInstance(6);
                break;

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, mFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                title = getString(R.string.title_section0);
                break;
            case 1:
                title = getString(R.string.title_section1);
                break;
            case 2:
                title = getString(R.string.title_section2);
                break;
            case 3:
                title = getString(R.string.title_section3);
                break;
            case 4:
                title = getString(R.string.title_section4);
                break;
            case 5:
                title = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationDrawerFragment navFrag = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navFrag.drawerToggle.setDrawerIndicatorEnabled(true);
        onSectionAttached(navFrag.currentSelectedPosition);
    }

    public void switchFragment(Fragment fragment, boolean clearBackStack) {
        if (fragment == null) {
            return;
        }
        if (clearBackStack)
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        if (!clearBackStack) transaction.addToBackStack(null);
        try {
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
