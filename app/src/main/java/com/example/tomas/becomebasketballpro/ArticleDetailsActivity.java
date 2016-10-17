package com.example.tomas.becomebasketballpro;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Tomas on 06/08/2016.
 */
public class ArticleDetailsActivity extends ActionBarActivity {

    private ImageView article_image;
    private TextView article_title;
    private TextView article_body;
    private TextView article_data;
 
    private InterstitialAd mInterstitialAd;

    String mPostKey;
    DatabaseReference mReference;

    public static final String EXTRA_POST_KEY = "post_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);



        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://basketball-training-app.firebaseio.com/article/");

        // Showing and Enabling clicks on the Home/Up button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setUpUIViews();

        mReference.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();

                article_title.setText(title);

                String body = (String) dataSnapshot.child("body").getValue();

                article_body.setText(Html.fromHtml(body).toString());

                String data = (String) dataSnapshot.child("published_date").getValue();

                article_data.setText("Added on: " + data);

                String image = (String) dataSnapshot.child("photo").getValue();

                Picasso.with(getBaseContext()).load(image).into(article_image);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ArticleDetailsActivity.this, "Failed to load...",
                        Toast.LENGTH_SHORT).show();
                FirebaseCrash.log(databaseError.toString());
            }
        });
    }

    private void setUpUIViews() {
        article_image = (ImageView)findViewById(R.id.article_image);
        article_title = (TextView)findViewById(R.id.article_title);
        article_body = (TextView)findViewById(R.id.article_body);
        article_data = (TextView)findViewById(R.id.article_data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onStop() {
        super.onStop();

    }
}

