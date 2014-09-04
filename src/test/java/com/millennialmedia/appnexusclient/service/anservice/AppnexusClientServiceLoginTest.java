package com.millennialmedia.appnexusclient.service.anservice;

import com.millennialmedia.appnexusclient.dto.appnexus.ANAuth;
import com.millennialmedia.appnexusclient.dto.appnexus.ANAuthWrapper;
import com.millennialmedia.appnexusclient.service.exception.LoginException;
import com.millennialmedia.appnexusclient.utils.JsonParserUtils;
import com.millennialmedia.appnexusclient.dto.appnexus.AccessToken;
import com.millennialmedia.appnexusclient.service.RequestType;
import com.millennialmedia.appnexusclient.service.ServiceHttpClientIf;
import org.apache.commons.collections.map.MultiValueMap;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 2/4/14
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Test(groups = "service.test")
public class AppnexusClientServiceLoginTest {
    private ANAuthWrapper auth = new ANAuthWrapper(new ANAuth("Jsilberman", "055b132a"));
    private AppnexusClientService service;

    @BeforeTest
    public void setUp(){
        service = new AppnexusClientServiceImpl();


    }
    @DataProvider
    public Object[][] loginTokenData() {
        // empty map
        Map<String, AccessToken> map0 =  new ConcurrentHashMap<String, AccessToken>();

        // an map with expired token
        Map<String, AccessToken> map =  new ConcurrentHashMap<String, AccessToken>();
        Calendar calendar  = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 120);
        Date expiredDate = calendar.getTime();
        AccessToken expiredToken = new AccessToken("expired", expiredDate);
        map.put("Jsilberman" + "end" , expiredToken);
        return new Object[][]{
               {map0, "/json/anresponse/success_token.json", "sbpf116qi3g1o9lhufu5m15bp5", true} ,
               {map, "/json/anresponse/success_token.json", "sbpf116qi3g1o9lhufu5m15bp5", true} ,
               {map, "/json/anresponse/failed_token.json", "", false} ,
        };
    }
    @Test(dataProvider = "loginTokenData")
    public void testCallANtoGetToken(Map<String, AccessToken> map, String file, String expectedToken, boolean isSucceed) throws Exception {

        String mockResponseBody = JsonParserUtils.getJsonFromFile(file, this);
        ANResponseInfo info = new ANResponseInfo(mockResponseBody, "application/json");
        ServiceHttpClientIf httpClient = Mockito.mock(ServiceHttpClientIf.class);
        Mockito.when(httpClient.makeRequest(Mockito.any(RequestType.class), Mockito.anyString(), Mockito.any(MultiValueMap.class), Mockito.anyString()))
              .thenReturn(info);

        Field field1 = AppnexusClientService.class.getDeclaredField("userExpirationMap");
        field1.setAccessible(true);
        field1.set(service, map);
        service.init(httpClient);
        try {
           String token = service.callAuthenticationService(auth, "end");
           if (isSucceed) {
               Assert.assertEquals(token, expectedToken);
           }
        } catch (LoginException e) {
           if (isSucceed){
               Assert.fail();
           }

        }
    }

    @Test
    public void testGetTokenFromMemory() throws Exception{
        Map<String, AccessToken> map =  new ConcurrentHashMap<String, AccessToken>();
        ServiceHttpClientIf httpClient = Mockito.mock(ServiceHttpClientIf.class);

        Calendar calendar  = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 50);
        Date validStartDate = calendar.getTime();
        AccessToken validToken = new AccessToken("valid token", validStartDate);
        map.put("Jsilberman" + "endpoint" , validToken);

        Field field = AppnexusClientService.class.getDeclaredField("userExpirationMap");
        field.setAccessible(true);
        field.set(service, map);

        String token = service.getToken(auth, "endpoint");
        Assert.assertEquals(token, "valid token");
    }

    public class AppnexusClientServiceImpl extends AppnexusClientService {

    }


}
