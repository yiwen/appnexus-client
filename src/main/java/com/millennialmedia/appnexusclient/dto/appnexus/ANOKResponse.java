package com.millennialmedia.appnexusclient.dto.appnexus;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 1/24/14
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */

public class ANOKResponse implements ANResponseIf {
    private String status;

    public String getStatus() {
        return status;
    }

    public boolean hasError() {
        return false;
    }

}
