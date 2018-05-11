package com.itexico.karen.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.itexico.karen.testapp.R;
import com.itexico.karen.testapp.models.Season;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_SEASON = "DetailsActivity.Season";
    private Season mSeason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mSeason = (Season) getIntent().getSerializableExtra(EXTRA_SEASON);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        ImageView backToolbarImg = findViewById(R.id.back_toolbar_img);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185" + mSeason.getThumbnail())
                .resize(400, 578)
                .centerCrop()
                .placeholder(R.drawable.season_background_placeholder)
                .error(R.drawable.season_background_placeholder)
                .into(backToolbarImg);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public static Intent newIntent(Context packageContext, Season item) {
        Intent i = new Intent(packageContext, DetailsActivity.class);
        i.putExtra(EXTRA_SEASON, item);
        return i;
    }
}
