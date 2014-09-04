package com.millennialmedia.appnexusclient.dto.appnexus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 1/24/14
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ANErrorResponse implements ANResponseIf {

    @JsonProperty("error_id")
    private String errorId;
    private String error;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_description")
    private String errorDescription;


    @JsonIgnore
    private String rawResponse;

    public String getErrorId() {
        return errorId;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean hasError() {
        return true;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    @Override
    public String toString() {
        return "ANErrorResponse{" +
                "errorId='" + errorId != null ? errorId:"" + '\'' +
                ", error='" + error != null ? error:"" + '\'' +
                ", errorCode='" + errorCode != null ? errorCode : "" + '\'' +
                ", errorDescription='" + errorDescription != null ? errorDescription : "" + '\'' +
                '}';
    }

    /**
     * if the response is not parseable, it will set all the attribute to null
     * @return
     */
   public boolean isEmpty(){
       return error == null && errorCode == null && errorId == null && errorDescription == null;
   }
}
