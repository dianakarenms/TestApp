package com.itexico.karen.testapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itexico.karen.testapp.R;
import com.itexico.karen.testapp.models.Episode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sagar on 10/25/2017.
 * this is the adapter that is used to populate the recyclerview in each activity.
 */
public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder>{

    private List<Episode> mEpisodeList = new ArrayList<>();

    public EpisodesAdapter(List<Episode> episodeList) {
        mEpisodeList = episodeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Episode episode = mEpisodeList.get(position);
        holder.mTitleTxt.setText(episode.getTitle());
        holder.mEpisodeNumberTxt.setText("E" + episode.getNumber());
    }

    @Override
    public int getItemCount() {
        return mEpisodeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitleTxt, mEpisodeNumberTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTxt = (TextView) itemView.findViewById(R.id.episode_title_txt);
            mEpisodeNumberTxt = (TextView) itemView.findViewById(R.id.episode_number_txt);
        }
    }
}
