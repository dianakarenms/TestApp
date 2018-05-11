package com.itexico.karen.testapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TmdbShow implements Serializable {
    @SerializedName("seasons")
    @Expose
    private List<TmdbSeason> seasons = null;

    public List<TmdbSeason> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<TmdbSeason> seasons) {
        this.seasons = seasons;
    }
}
