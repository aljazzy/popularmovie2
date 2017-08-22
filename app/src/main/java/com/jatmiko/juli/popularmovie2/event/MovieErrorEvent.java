package com.jatmiko.juli.popularmovie2.event;

/**
 * Created by Miko on 10/08/2017.
 */

public class MovieErrorEvent extends Event {
    public MovieErrorEvent(String message) {
        super(message);
    }
}
