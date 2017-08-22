package com.jatmiko.juli.popularmovie2.event;

/**
 * Created by Miko on 10/08/2017.
 */

public class Event {
    private String message;

    Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
