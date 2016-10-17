package com.example.tomas.becomebasketballpro;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
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
 * Created by Tomas on 09/08/2016.
 */
public class MotivationDetailsActivity extends ActionBarActivity {

    private ImageView article_image;

    private InterstitialAd mInterstitialAd;

    String mPostKey;
    DatabaseReference mReference;

    public static final String EXTRA_POST_KEY = "post_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motivation_detail);

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

        // Initialize Database
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://basketball-training-app.firebaseio.com/motivation/");

        // Showing and Enabling clicks on the Home/Up button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // setting up text views and stuff
        setUpUIViews();


    }

    private void setUpUIViews() {
        article_image = (ImageView)findViewById(R.id.article_image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        mReference.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String image = (String) dataSnapshot.child("photo").getValue();

                // Then later, when you want to display image
                Picasso.with(getBaseContext()).load(image).into(article_image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MotivationDetailsActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                FirebaseCrash.log(databaseError.toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
