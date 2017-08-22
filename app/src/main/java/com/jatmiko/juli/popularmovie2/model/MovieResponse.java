package com.jatmiko.juli.popularmovie2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Miko on 09/08/2017.
 */

public class MovieResponse {
    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }
}
