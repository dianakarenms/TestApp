package com.itexico.karen.testapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itexico.karen.testapp.R;
import com.itexico.karen.testapp.activities.DetailsActivity;
import com.itexico.karen.testapp.models.Season;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeasonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Season> mEpisodesList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Season item;
        public TextView mSeasonNumberTxt, mEpisodesCountTxt, mRatingTxt;
        public ImageView mThumnailImg;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mSeasonNumberTxt = view.findViewById(R.id.season_txt);
            mEpisodesCountTxt = view.findViewById(R.id.ep_count_txt);
            mRatingTxt = view.findViewById(R.id.rating_txt);
            mThumnailImg = view.findViewById(R.id.thumbnail_img);
        }

        public void setItem(Season item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            context.startActivity(DetailsActivity.newIntent(context, item));
        }
    }

    public SeasonRecyclerAdapter(List<Season> episodesList) {
        mEpisodesList = episodesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_season_thumbnail, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Season season = mEpisodesList.get(position);
        viewHolder.setItem(season);
        viewHolder.mSeasonNumberTxt.setText(season.getTitle());
        viewHolder.mEpisodesCountTxt.setText("Episodes: " + season.getAiredEpisodes());

        String formatedRating = String.format("%.2f", season.getRating());
        viewHolder.mRatingTxt.setText("Rating: " + formatedRating);

        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185" + season.getThumbnail())
                .resize(400, 578)
                .placeholder(R.drawable.serie_thumbnail_placeholder)
                .error(R.drawable.season_background_placeholder)
                .into(viewHolder.mThumnailImg);


    }

    @Override
    public int getItemCount() {
        return mEpisodesList.size();
    }
}
