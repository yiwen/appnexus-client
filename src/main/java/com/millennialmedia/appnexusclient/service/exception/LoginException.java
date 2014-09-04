package com.millennialmedia.appnexusclient.service.exception;

/**
 * A custom exception that is thrown by the login utility to indicate error conditions that need to be handled in client code.
 * 
 * @author gyang
 * @since 1.0
 */
public class LoginException extends Exception {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
