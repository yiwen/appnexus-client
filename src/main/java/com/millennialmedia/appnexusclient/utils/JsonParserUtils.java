package com.millennialmedia.appnexusclient.utils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 2/3/14
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonParserUtils {

    public static String getJsonFromFile(String file,  Object o) throws FileNotFoundException, IOException {
       URL url = o.getClass().getResource(file);
       if (url == null) {
           throw new FileNotFoundException(file);
       }
       BufferedInputStream inputStream = new BufferedInputStream(o.getClass().getResourceAsStream(file));

       StringWriter writer = new StringWriter();
       IOUtils.copy(inputStream, writer, "UTF-8");
       String json = writer.toString();
       return json;
    }
}
