package com.jatmiko.juli.popularmovie2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Miko on 18/08/2017.
 */

public class ReviewResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Review> results;

    public int getId() {
        return id;
    }

    public List<Review> getResults() {
        return results;
    }
}
