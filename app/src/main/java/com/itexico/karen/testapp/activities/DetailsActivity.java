package com.itexico.karen.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.itexico.karen.testapp.R;
import com.itexico.karen.testapp.adapters.EpisodesAdapter;
import com.itexico.karen.testapp.api.ApiClient;
import com.itexico.karen.testapp.api.ApiConfig;
import com.itexico.karen.testapp.models.Episode;
import com.itexico.karen.testapp.models.Season;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailsActivity extends AppCompatActivity
    implements AppBarLayout.OnOffsetChangedListener {

    private static final String EXTRA_SEASON = "CoordinatorActivity.Season";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_SHOW_TOOLBAR           = 0.4f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private RelativeLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ImageView mBackgroundImg, mThumnailImg;
    private ArrayList<Episode> mEpisodesList = new ArrayList<>();
    private EpisodesAdapter mEpisodesAdapter;
    private Season mSeason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bindActivity();

        mSeason = (Season) getIntent().getSerializableExtra(EXTRA_SEASON);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_coordinator_behavior);

        mBackgroundImg = (ImageView) findViewById(R.id.main_background_img);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185" + mSeason.getThumbnail())
                .resize(400, 578)
                .centerCrop()
                .placeholder(R.drawable.season_background_placeholder)
                .error(R.drawable.season_background_placeholder)
                .into(mBackgroundImg);

        mThumnailImg = (ImageView) findViewById(R.id.thumbnail_img);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185" + mSeason.getThumbnail())
                .resize(400, 578)
                .centerCrop()
                .placeholder(R.drawable.serie_thumbnail_placeholder)
                .error(R.drawable.serie_thumbnail_placeholder)
                .into(mThumnailImg);

        ((TextView) findViewById(R.id.toolbar_title_txt)).setText(mSeason.getTitle());
        ((TextView) findViewById(R.id.details_title_txt)).setText(mSeason.getTitle());
        ((TextView) findViewById(R.id.details_episodes_txt)).setText("Episodes: " + mSeason.getAiredEpisodes());
        ((TextView) findViewById(R.id.details_votes_txt)).setText("Votes: " + mSeason.getVotes());
        String formatedRating = String.format("%.1f", mSeason.getRating());
        ((TextView) findViewById(R.id.details_rating_txt)).setText(formatedRating);

        ((ImageButton) findViewById(R.id.back_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mEpisodesAdapter = new EpisodesAdapter(mEpisodesList);
        mRecyclerView.setAdapter(mEpisodesAdapter);

        mAppBarLayout.addOnOffsetChangedListener(this);

        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        loadSeasonEpisodes();
    }

    private void bindActivity() {
        mToolbar        = findViewById(R.id.main_toolbar);
        mTitle          = findViewById(R.id.toolbar_title_txt);
        mTitleContainer = findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = findViewById(R.id.main_appbar);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
        handleToolbarVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
            if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

                if(!mIsTheTitleVisible) {
                    startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE); mIsTheTitleVisible = true;
                }

            } else {

                if (mIsTheTitleVisible) {
                    startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);mIsTheTitleVisible = false;
                }
            }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {

            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }

        }
    }

    private void handleToolbarVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TOOLBAR) {
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.INVISIBLE);
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
            ? new AlphaAnimation(0f, 1f)
            : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void loadSeasonEpisodes() {
        String url = String.format(ApiConfig.seasons, String.valueOf(mSeason.getNumber()));
        ApiClient.getInstance(this).addToRequestQueue(new ApiClient.GsonRequest<>(
                url,
                Episode[].class,
                Request.Method.GET,
                ApiConfig.getTraktHeaders(),
                null,
                new Response.Listener<Episode[]>() {
                    @Override
                    public void onResponse(Episode[] response) {
                        mEpisodesList.addAll(Arrays.asList(response));
                        mEpisodesAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Load Seasons", "Error ocurred");
                    }
                }, true
        ));
    }

    public static Intent newIntent(Context packageContext, Season item) {
        Intent i = new Intent(packageContext, DetailsActivity.class);
        i.putExtra(EXTRA_SEASON, item);
        return i;
    }
}
