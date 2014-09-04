package com.millennialmedia.appnexusclient.service;

import com.millennialmedia.appnexusclient.service.anservice.ANResponseInfo;
import com.millennialmedia.appnexusclient.service.exception.ANErrorResponseException;
import org.apache.commons.collections.MultiMap;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 1/26/14
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceHttpClientIf {
    public ANResponseInfo makeRequest(RequestType requestType,String url, MultiMap headers,String postBody) throws ANErrorResponseException;
}
