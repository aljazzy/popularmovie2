package com.jatmiko.juli.popularmovie2.event;

import com.jatmiko.juli.popularmovie2.model.VideoResponse;

/**
 * Created by julijatmiko on 8/20/17.
 */

public class MvTrailerEvent extends Event {
    private VideoResponse body;

    public MvTrailerEvent(String message, VideoResponse body) {
        super(message);
        this.body = body;
    }

    public VideoResponse getBody() {
        return body;
    }
}
