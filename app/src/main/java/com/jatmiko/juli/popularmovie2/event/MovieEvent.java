package com.jatmiko.juli.popularmovie2.event;

import com.jatmiko.juli.popularmovie2.model.Movie;
import com.jatmiko.juli.popularmovie2.utility.Constant;

import java.util.List;

/**
 * Created by Miko on 10/08/2017.
 */

public class MovieEvent extends Event {
    private final List<Movie> results;
    private final Constant.Data.MOVIE_LIST_TITLE movieListTitle;

    public MovieEvent(String message, List<Movie> results, Constant.Data.MOVIE_LIST_TITLE movieListTitle) {
        super(message);
        this.results = results;
        this.movieListTitle = movieListTitle;
    }

    public Constant.Data.MOVIE_LIST_TITLE getMvListTitle() {
        return movieListTitle;
    }

    public List<Movie> getResults() {
        return results;
    }

}
