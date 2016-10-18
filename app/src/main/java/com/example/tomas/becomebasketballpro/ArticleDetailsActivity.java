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

    private ImageView articleImage;
    private TextView articleTitle;
    private TextView articleBody;
    private TextView articleData;

    private InterstitialAd interstitialAd;

    String postKey;
    DatabaseReference reference;

    public static final String EXTRA_POST_KEY = "post_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);


        interstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        postKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (postKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://basketball-training-app.firebaseio.com/article/");

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setUpUIViews();

        reference.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();

                articleTitle.setText(title);

                String body = (String) dataSnapshot.child("body").getValue();

                articleBody.setText(Html.fromHtml(body).toString());

                String data = (String) dataSnapshot.child("published_date").getValue();

                articleData.setText("Added on: " + data);

                String image = (String) dataSnapshot.child("photo").getValue();

                Picasso.with(getBaseContext()).load(image).into(articleImage);

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
        articleImage = (ImageView) findViewById(R.id.image_article);
        articleTitle = (TextView) findViewById(R.id.text_title);
        articleBody = (TextView) findViewById(R.id.text_body);
        articleData = (TextView) findViewById(R.id.text_data);
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
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
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

