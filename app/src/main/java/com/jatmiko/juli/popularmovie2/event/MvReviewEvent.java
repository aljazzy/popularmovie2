package com.jatmiko.juli.popularmovie2.event;

import com.jatmiko.juli.popularmovie2.model.ReviewResponse;

/**
 * Created by julijatmiko on 8/20/17.
 */

public class MvReviewEvent extends Event {
    private ReviewResponse body;

    public MvReviewEvent(String message, ReviewResponse body) {
        super(message);
        this.body = body;
    }

    public ReviewResponse getBody() {
        return body;
    }
}
