package com.millennialmedia.appnexusclient.dto.appnexus;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 1/24/14
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */

public class ANOKResponse implements ANResponseIf {
    private String status;

    @JsonIgnore
    private String rawResponse;

    public String getStatus() {
        return status;
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
