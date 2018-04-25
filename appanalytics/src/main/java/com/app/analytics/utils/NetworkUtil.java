package com.app.analytics.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Sanjay.k on 4/14/2017.
 */

public class NetworkUtil {

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

    public static String getUUID(HttpResponse httpResponse) {

        return getUUID(httpResponse.getAllHeaders());
    }

    public static String getUUID(Header[] headers) {

        for (Header header : headers) {
            if (header.getName().trim().equalsIgnoreCase(AnalyticsAppConstants.UUID)) {
                return header.getValue().toString().trim();
            }
        }
        return "-1";
    }
}
