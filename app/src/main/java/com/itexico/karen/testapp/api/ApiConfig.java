package com.itexico.karen.testapp.api;

import com.itexico.karen.testapp.BuildConfig;

import java.util.HashMap;

public class ApiConfig {

    private static final String seriesInfo = BuildConfig.TRAKT_API_URL.concat("shows/breaking-bad/");
    public static final String seasons = seriesInfo.concat("seasons/%1$s?extended=full");

    public static final String thumbnails = BuildConfig.TMDB_API_URL.concat("1396?api_key=fb7bb23f03b6994dafc674c074d01761&language=en-US&include_image_language=en,null");

    //public static final String baseUrlTMDB = "http://api.themoviedb.org/3/tv/1399/season/2/images?api_key=fb7bb23f03b6994dafc674c074d01761&language=en-US&include_image_language=en,null";


    /**
     * Authenticated header for all api requests, makes use of users' access token
     * @return headers
     */
    public static HashMap<String, String> getTraktHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("trakt-api-key", BuildConfig.TRAKT_API_KEY);
        headers.put("trakt-api-version", "2");
        return  headers;
    }

    public static HashMap<String, String> getTvdbHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("api_key", BuildConfig.TMDB_API_KEY);
        return headers;
    }
}
