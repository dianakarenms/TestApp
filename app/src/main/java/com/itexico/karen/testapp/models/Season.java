package com.itexico.karen.testapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Season implements Serializable {
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("ids")
    @Expose
    private Ids ids;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("aired_episodes")
    @Expose
    private Integer airedEpisodes;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("votes")
    @Expose
    private String votes;
    private String thumbnail;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getAiredEpisodes() {
        return airedEpisodes;
    }

    public void setAiredEpisodes(Integer airedEpisodes) {
        this.airedEpisodes = airedEpisodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
