package com.millennialmedia.appnexusclient.service.anservice;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 7/15/14
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AppnexusClientServiceIf {
    public AppnexusClientService.AppnexusClient buildClient(String endPoint, String userName, String password);
}
