package com.millennialmedia.appnexusclient.service.anservice;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 9/3/14
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ANResponseInfo {

    private String response;
    private String contentType;

    public ANResponseInfo() {
    }

    public ANResponseInfo(String response, String contentType) {
        this.response = response;
        this.contentType = contentType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
