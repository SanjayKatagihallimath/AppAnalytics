package com.app.analytics.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Sanjay.k on 5/9/2017.
 */

public class CustomHttpException extends Exception {

    private final String url;
    private final int code;
    private final String message;
    private final Header[] headers;
    private final Headers okHttp3Headers;
    private final String uuid;
    private String type;

    public String getUrl() {
        return url;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public Headers getOkHttp3Headers() {
        return okHttp3Headers;
    }

    public CustomHttpException(String url, HttpResponse httpResponse) {

        this.url = url;
        this.code = httpResponse.getStatusLine().getStatusCode();
        this.message = httpResponse.getStatusLine().getReasonPhrase();

        this.headers = httpResponse.getAllHeaders();
        this.okHttp3Headers = null;
        this.uuid = NetworkUtil.getUUID(this.headers);

        try {
            this.type = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CustomHttpException(Response<ResponseBody> response) {

        this.url = response.raw().request().url().toString();
        this.code = response.code();
        this.message = response.message();

        this.headers = null;
        this.okHttp3Headers = response.raw().headers();
        this.uuid = (okHttp3Headers.get(AnalyticsAppConstants.UUID) != null) ? okHttp3Headers.get(AnalyticsAppConstants.UUID) : "-1";

        try {
            this.type = NetworkUtil.convertInputStreamToString(response.errorBody().byteStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
