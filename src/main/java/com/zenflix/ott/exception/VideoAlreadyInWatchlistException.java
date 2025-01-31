package com.zenflix.ott.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VideoAlreadyInWatchlistException extends RuntimeException {
    public VideoAlreadyInWatchlistException(String message) {
        super(message);
    }
}
