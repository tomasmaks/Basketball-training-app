package com.example.tomas.becomebasketballpro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.tomas.becomebasketballpro.dummy.DummyContent;
import com.example.tomas.becomebasketballpro.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ArticleListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    public View mView;
    private ArticleListAdapter mAdapter;

   // private ArticleListAdapter mAdapter;

    public ArticleListFragment() {
        // Required empty public constructor



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        mSwipeRefreshLayout.setOnRefreshListener(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_article_list, container,
                false);


        int columnCount = getResources().getInteger(R.integer.grid_column_count);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        mAdapter = new ArticleListAdapter(new ArrayList<Data>());
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }



    @Override
    public void onRefresh() {

    }

//    @Override
//    public void open(Data data, int position) {
//        ArticleDetailFragment fragment = new ArticleDetailFragment();
//        this.getFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment,TAG_FRAGMENT)
//                .addToBackStack(null)
//                .commit();
//
//    }

//    @Override
//    public void onLoadFinished(Cursor cursor) {
//        mAdapter.swapCursor(cursor);
//    }
//
//    @Override
//    public void onLoaderReset() {
//        mAdapter.swapCursor(null);
//    }



}
