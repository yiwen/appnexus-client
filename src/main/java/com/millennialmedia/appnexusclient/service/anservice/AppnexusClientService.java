package com.millennialmedia.appnexusclient.service.anservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.millennialmedia.appnexusclient.dto.appnexus.*;
import com.millennialmedia.appnexusclient.service.RequestType;
import com.millennialmedia.appnexusclient.service.ServiceHttpClientIf;
import com.millennialmedia.appnexusclient.service.exception.ANErrorResponseException;
import com.millennialmedia.appnexusclient.service.exception.LoginException;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 6/16/14
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AppnexusClientService implements AppnexusClientServiceIf {

    private static final Logger LOG = LoggerFactory.getLogger(AppnexusClient.class);
    private ServiceHttpClientIf serviceHttpClientIf;
    private ObjectMapper mapper;
    private final Map<String, AccessToken> userExpirationMap = new ConcurrentHashMap<String, AccessToken>();
    private static final long defaultExpirationTimeMillis = 6000000L; //100mins
    private static final String HEADER_AUTH_KEY = "Authorization";
    private static final String AUTH_PATH = "/auth";
    /**
     * override init() in your own implementation to set ServiceHttpClientIf and other necessary logic
     */
    public void init(ServiceHttpClientIf serviceHttpClientIf){
        if (serviceHttpClientIf == null) {
            throw new IllegalArgumentException("ServiceHttpClientIf can't be null");
        }
        this.serviceHttpClientIf = serviceHttpClientIf;
        initObjectMapper();

    }


    public AppnexusClient buildClient(String endPoint, String userName, String password) {
        if (serviceHttpClientIf == null) {
            throw new InstantiationError("please provide implementation of ServiceHttpClientIf");
        }
        ANAuthWrapper anAuth = new ANAuthWrapper(new ANAuth(userName, password));
        AppnexusClient client =  new AppnexusClient(endPoint, anAuth, this);
        return client;
    }

    protected String getToken(ANAuthWrapper auth, String serviceEndpoint) throws LoginException, ANErrorResponseException {
        if (auth == null) {
           throw new IllegalArgumentException("auth cannot be null");
        }

        if (serviceEndpoint == null) {
           throw new IllegalArgumentException("serviceEndpoint cannot be null");
        }

        String appnexusToken;
        if (userExpirationMap.get(auth.getUserCredentials().getUsername() + serviceEndpoint) != null) {
           AccessToken accessToken = userExpirationMap.get(auth.getUserCredentials().getUsername() + serviceEndpoint);
           Date startDate = accessToken.getStartDate();
           Date currentDate = new Date();
           if (currentDate.getTime() - startDate.getTime() > defaultExpirationTimeMillis) {
               // current token expired. request a new token
                appnexusToken = callAuthenticationService(auth, serviceEndpoint);
                userExpirationMap.put(auth.getUserCredentials().getUsername() + serviceEndpoint, new AccessToken(appnexusToken, new Date()));

           }
           else {
               // otherwise, return current token
               return accessToken.getAppnexusToken();
           }
        } else {
           // request a new token
            appnexusToken = callAuthenticationService(auth, serviceEndpoint);
            // callAuthenticationService shouldn't return "" or null
            // just to ensure, so when it gets invalid, we dont populate the map
            if (appnexusToken !=null && appnexusToken.length() > 0){
                userExpirationMap.put(auth.getUserCredentials().getUsername() + serviceEndpoint, new AccessToken(appnexusToken, new Date()));
            }

        }
        return appnexusToken;
    }

    protected String callAuthenticationService(final ANAuthWrapper auth, final String serviceEndpoint) throws ANErrorResponseException, LoginException {
        try {
            TypeReference type = new TypeReference<ANResponse<ANTokenResponse>>(){};
            String postBody = mapper.writeValueAsString(auth);
            ANResponseInfo info= serviceHttpClientIf.makeRequest(RequestType.POST, serviceEndpoint + AUTH_PATH,
                    new MultiValueMap(), postBody);
            String responseBody = info.getResponse();
            ANResponse<ANTokenResponse> response = mapper.readValue(responseBody, type);
            if (response != null && response.getResponse() != null){
                if("OK".equals(response.getResponse().getStatus())) {
                    String token = response.getResponse().getToken();
                    return token;
                }
                else {
                    TypeReference responseType = new TypeReference<ANResponse<ANErrorResponse>>() {};
                    ANResponse<ANErrorResponse> aNerroResponse= mapper.readValue(responseBody, responseType);
                    if (aNerroResponse == null) {
                        throw new LoginException("Unparseable auth failed response:" + response);
                    }
                    if(aNerroResponse.getResponse().getErrorId()!=null){
                        throw new LoginException(aNerroResponse.getResponse().getErrorId() + ":"+ aNerroResponse.getResponse().getErrorDescription() + "| code:" +aNerroResponse.getResponse().getErrorCode());
                    }
                }

            }

        }
        catch(JsonProcessingException e) {
            LOG.error("Error occurred in serializing auth", e);
            throw new LoginException(e.getMessage(), e);
        }
        catch (IOException e) {
            LOG.error("Error occurred in deserializing auth response", e);
            throw new LoginException(e.getMessage(), e);
        }
        LOG.error("failed to get token");
        throw new LoginException("failed to get token");

    }

    @SuppressWarnings("unchecked")
    public ANResponseIf makeRequestToAN(ANAuthWrapper anAuth, String anUrl, RequestType requestType, String path, String postBody, TypeReference responseType, MultiMap headers) throws LoginException, ANErrorResponseException {
        String token = getToken(anAuth, anUrl);

        MultiMap newHeaders =  new MultiValueMap();
        if (headers!=null) {
           newHeaders = headers;
           newHeaders.put(HEADER_AUTH_KEY, token);
        } else {
           newHeaders.put(HEADER_AUTH_KEY, token);
        }

        String url = anUrl + path;
        ANResponseInfo info = serviceHttpClientIf.makeRequest(requestType, url, newHeaders,postBody);
        String response = info.getResponse();
        String contentType = info.getContentType();

        try {
            if ("application/json".equals(contentType)) {
                  // if it can be parsed into an OK response
                ANResponse<ANOKResponse> anResponse= mapper.readValue(response, responseType);
                if (anResponse != null && anResponse.getResponse()!=null){
                    if ("OK".equals(anResponse.getResponse().getStatus())){
                        anResponse.getResponse().setRawResponse(response);
                        return anResponse.getResponse();
                    } else {
                        // if it can be parsed into an error response
                        ANErrorResponse errorResponse = parseErrorResponse(response);
                        if (errorResponse != null){
                            return errorResponse;
                        } else {
                            throw new ANErrorResponseException("Unparseable Response:" + response);
                        }

                    }
                } else {
                    throw new ANErrorResponseException("Unparseable Response:" + response);
                }
            } else if ("text/html".equals(contentType)) {
                ANStringResponse stringResponse = new ANStringResponse(response);
                return stringResponse;
            } else {
                throw new ANErrorResponseException("Unsupported Response content-type:" + contentType + "response: " + response);
            }

        } catch (IOException e) {
            LOG.error("Error occurred in deserializing response", e);
            throw new ANErrorResponseException(e.getMessage(), e);
        }

    }


    private ANErrorResponse parseErrorResponse(String response) throws IOException {

        TypeReference errorType = new TypeReference<ANResponse<ANErrorResponse>>(){};
        ANResponse<ANErrorResponse> anErroResponse= mapper.readValue(response, errorType);

        if (anErroResponse != null && anErroResponse.getResponse() != null && !anErroResponse.getResponse().isEmpty()) {
            anErroResponse.getResponse().setRawResponse(response);
            return anErroResponse.getResponse();
        }
        return null;

    }

    private void initObjectMapper() {
        this.mapper= new ObjectMapper();
        // SerializationFeature for changing how JSON is written
        // to enable standard indentation ("pretty-printing"):
        //  mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // to allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // to write java.util.Date, Calendar as number (timestamp):
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // DeserializationFeature for changing how JSON is read as POJOs:

        // to prevent exception when encountering unknown property:
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow coercion of JSON empty String ("") to null Object value:
        //mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }

    public void setServiceHttpClientIf(ServiceHttpClientIf serviceHttpClientIf) {
        if (serviceHttpClientIf == null) {
            throw new IllegalArgumentException("ServiceHttpClientIf can't be null");
        }
        this.serviceHttpClientIf = serviceHttpClientIf;
    }


    public static class AppnexusClient {

        private static final Logger LOG = LoggerFactory.getLogger(AppnexusClient.class);
        private AppnexusClientService  service;
        private String anUrl;
        private ANAuthWrapper anAuth;


        private AppnexusClient(String anUrl, ANAuthWrapper anAuth, AppnexusClientService service) {
            this.anUrl = anUrl;
            this.anAuth = anAuth;
            this.service = service;
        }
        public ANResponseIf makeRequest(RequestType requestType, String path, String postBody, TypeReference responseType,  MultiMap headers)
                throws LoginException, ANErrorResponseException {
            return service.makeRequestToAN(anAuth, anUrl, requestType, path, postBody, responseType, headers);
        }


    }
}
