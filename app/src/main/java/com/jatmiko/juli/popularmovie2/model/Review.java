package com.jatmiko.juli.popularmovie2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Miko on 18/08/2017.
 */

public class Review {
    @SerializedName("id")
    private String reviewId;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
