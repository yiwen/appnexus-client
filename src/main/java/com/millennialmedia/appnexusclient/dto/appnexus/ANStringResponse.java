package com.millennialmedia.appnexusclient.dto.appnexus;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 9/3/14
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ANStringResponse implements ANResponseIf {

    private String responseStr;

    public ANStringResponse(String responseStr) {
        this.responseStr = responseStr;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }

    public boolean hasError() {
      return false;
    }
}
