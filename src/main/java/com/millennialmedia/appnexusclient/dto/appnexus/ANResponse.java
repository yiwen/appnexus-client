package com.millennialmedia.appnexusclient.dto.appnexus;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 1/29/14
 * Time: 8:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class ANResponse<R extends ANResponseIf> {

    private R response;

    public R getResponse() {
        return response;
    }

    public void setResponse(R response) {
        this.response = response;
    }
}
