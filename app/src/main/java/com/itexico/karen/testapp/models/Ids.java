package com.itexico.karen.testapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ids implements Serializable {
    @SerializedName("trakt")
    @Expose
    private Integer trakt;
    @SerializedName("tmdb")
    @Expose
    private Integer tmdb;

    public Integer getTrakt() {
        return trakt;
    }

    public void setTrakt(Integer trakt) {
        this.trakt = trakt;
    }

    public Integer getTmdb() {
        return tmdb;
    }

    public void setTmdb(Integer tmdb) {
        this.tmdb = tmdb;
    }
}
