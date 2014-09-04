package com.millennialmedia.appnexusclient.service.exception;

/**
 * Represents an error when an response with an error
 */
public class ANErrorResponseException extends Exception {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    public ANErrorResponseException(String message)
    {
        super(message);
    }

    public ANErrorResponseException(String message, Throwable ex) {
        super(message, ex);
    }
}
