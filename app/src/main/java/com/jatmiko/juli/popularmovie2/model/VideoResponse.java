package com.jatmiko.juli.popularmovie2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Miko on 18/08/2017.
 */

public class VideoResponse {
    @SerializedName("id")
    private int mvId;

    @SerializedName("results")
    private List<Video> results;

    public int getMvId() {
        return mvId;
    }

    public List<Video> getResults() {
        return results;
    }


}
