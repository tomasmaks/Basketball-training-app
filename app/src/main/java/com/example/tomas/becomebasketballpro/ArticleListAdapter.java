package com.example.tomas.becomebasketballpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Tomas on 29/07/2016.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {


    //private List<StoryListItemViewModel> mDataset;
    private Context mContext;

    public View mView;
    private final ArrayList<Data> mData;



    public ArticleListAdapter(ArrayList<Data> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        RelativeLayout view = (RelativeLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.include_article_list, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                        ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))));
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //StoryListItemViewModel item = mDataset.get(position);

        TextView scoreText = (TextView) holder.titleView.findViewById(R.id.article_title);
        TextView domainText = (TextView) holder.subtitleView.findViewById(R.id.article_subtitle);
        TextView titleText = (TextView) holder.authorView.findViewById(R.id.article_author);
        DynamicHeightNetworkImageView featureImage = (DynamicHeightNetworkImageView) holder.thumbnailView.findViewById(R.id.thumbnail);

//        holder.setClickListener(mClickListener);
//
//        scoreText.setText(item.getScore());
//        domainText.setText(item.getDomain());
//        titleText.setText(item.getTitle());
//        commentCountText.setText(item.getCommentCount());
//        relativeTimeText.setText(item.getRelativeTime());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DynamicHeightNetworkImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;
        public TextView authorView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
            authorView = (TextView) view.findViewById(R.id.article_author);
        }
    }
}
