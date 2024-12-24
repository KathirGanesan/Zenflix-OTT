package com.streamhub.ott.exception;

public class SubscriptionConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public SubscriptionConflictException(String message) {
        super(message);
    }
}

