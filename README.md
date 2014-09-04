appnexus-client
===============
this library provides login service and dto classes and an interface to use your own implementation of httpclient for using appnexus api service,
see https://wiki.appnexus.com/display/adnexusdocumentation/Impression+Bus+API

Appnexus api requires an unexpired auth to be passed into the header for each api service, this library baked the login logic into the service, the end user no longer needs to worry about it.
the library also provides dto classes for both success response and error response, the end user can extend the success response ```ANOKResponse```  based on the response they are getting.

Currenlty, it only handle two types of content-type of the response, 'applicaiton/json' and 'text/html'. When the content type is 
'applicaiton/json', it will try to parse the response into the class you passed in, when the response is 'text/html', it will return the string as response.

How to use it:


1.  Create a project with the dependency
2.  Extend AppnexusClientService to create your own service class. Implement ServiceHttpClientIf using your own httpclient.
3.  Create one or more client by ```service.buildClient(END_POINT, USER_NAME, PASSWORD)```, you can also override ```buildClient()``` to use your own ```ANresponseHandler```
4.   Call ```client.makeGetRequestToAN``` etc to make request



    ```
      <dependency>
                    <groupId>com.millennialmedia</groupId>
                    <artifactId>appnexus-client</artifactId>
                    <version>1.0-SNAPSHOT</version>
               </dependency>
    ```
    


    For example:
    ```
    public class AppnexusClientMMService extends AppnexusClientService {
    .....
     @Override
        public void init(){
            super.init();
            ServiceHttpClientIf myclient = new ServiceHttpClientImpl();
            this.setServiceHttpClientIf(myclient);
        }
    ```



 
