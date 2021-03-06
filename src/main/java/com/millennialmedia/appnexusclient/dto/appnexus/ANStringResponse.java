package com.millennialmedia.appnexusclient.dto.appnexus;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 9/3/14
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ANStringResponse implements ANResponseIf {

    private String rawResponse;

    public ANStringResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public boolean hasError() {
      return false;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }
}
