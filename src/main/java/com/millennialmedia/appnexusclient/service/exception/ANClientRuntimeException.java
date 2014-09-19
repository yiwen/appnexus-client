package com.millennialmedia.appnexusclient.service.exception;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 9/4/14
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ANClientRuntimeException extends RuntimeException{
    public ANClientRuntimeException() {
    }

    public ANClientRuntimeException(String message) {
        super(message);
    }

    public ANClientRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
