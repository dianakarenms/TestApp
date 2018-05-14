package com.itexico.karen.testapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.itexico.karen.testapp.R;
import com.itexico.karen.testapp.adapters.SeasonRecyclerAdapter;
import com.itexico.karen.testapp.api.ApiClient;
import com.itexico.karen.testapp.api.ApiConfig;
import com.itexico.karen.testapp.models.Season;
import com.itexico.karen.testapp.models.TmdbSeason;
import com.itexico.karen.testapp.models.TmdbShow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SeasonRecyclerAdapter mAdapter;
    private List<Season> mSeasonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action);

        mRecyclerView = findViewById(R.id.seasons_recycler);

        mAdapter = new SeasonRecyclerAdapter(mSeasonList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        loadSeasons();

    }

    private void loadSeasons() {
        String url = String.format(ApiConfig.seasons, "");
        ApiClient.getInstance(this).addToRequestQueue(new ApiClient.GsonRequest<>(
                url,
                Season[].class,
                Request.Method.GET,
                ApiConfig.getTraktHeaders(),
                null,
                new Response.Listener<Season[]>() {
                    @Override
                    public void onResponse(Season[] response) {
                        mSeasonList.addAll(Arrays.asList(response));
                        loadThumbnails();
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

    private void loadThumbnails() {
        ApiClient.getInstance(this).addToRequestQueue(new ApiClient.GsonRequest<>(
                ApiConfig.thumbnails,
                TmdbShow.class,
                Request.Method.GET,
                null,
                null,
                new Response.Listener<TmdbShow>() {
                    @Override
                    public void onResponse(TmdbShow response) {
                        List<TmdbSeason> seasonsThumbnails = response.getSeasons();
                        for(int i = 0; i < seasonsThumbnails.size(); i++) {
                            // TODO: Make sure the thumbnail corresponds to the season's data
                            mSeasonList.get(i).setThumbnail(seasonsThumbnails.get(i).getPosterPath());
                        }
                        mAdapter.notifyDataSetChanged();
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
}
