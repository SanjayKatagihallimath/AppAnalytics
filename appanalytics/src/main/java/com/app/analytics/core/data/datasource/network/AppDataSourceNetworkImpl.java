package com.app.analytics.core.data.datasource.network;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.app.analytics.core.domain.model.AnalyticEvent;
import com.app.analytics.core.domain.model.Token;
import com.app.analytics.utils.AnalyticsAppConstants;
import com.app.analytics.utils.ApiConstants;
import com.app.analytics.utils.CustomHttpException;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by Sanjay.k on 4/1/2017.
 */

public class AppDataSourceNetworkImpl implements AppDataSource {
    public static final String TAG = AppDataSourceNetworkImpl.class.getSimpleName();

    private final AppDataService appDataService,appAnalyticsDataService;
    private final SharedPreferences sharedPreferences;

    public AppDataSourceNetworkImpl(AppDataService appDataService,AppDataService appAnalyticsDataService, SharedPreferences sharedPreferences) {

        this.appDataService = appDataService;
        this.appAnalyticsDataService = appAnalyticsDataService;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Observable<Response<Void>> getSessionToken() {

        return appDataService.getSessionToken(AnalyticsAppConstants.authToken);
    }

    @Override
    public Observable<Token> requestSessionToken() {

        return Observable.create((ObservableEmitter<Token> emitter) -> {
            try {
                Log.d(TAG, "Session Token HttpClient Request.");
                HttpPost httpPost = new HttpPost(ApiConstants.SESSION_TOKEN_URL);
                httpPost.setEntity(new StringEntity(AnalyticsAppConstants.authToken));
                httpPost.setHeader(ApiConstants.CONTENT_TYPE, ApiConstants.CONTENT_TYPE_FORMAT_TEXT_PLAIN);
                httpPost.setHeader(ApiConstants.ACCEPT_TYPE, ApiConstants.ACCEPT_TYPE_FORMAT_V4_JSON);

                DefaultHttpClient httpClient = new DefaultHttpClient(httpPost.getParams());
                emitResponse(ApiConstants.SESSION_TOKEN_URL, httpClient.execute(httpPost), emitter);
            } catch (Throwable t) {
                emitter.onError(t);
            }
        });
    }

    @Override
    public Observable<Token> requestBearerToken() {

        return Observable.create((ObservableEmitter<Token> emitter) -> {
            try {
                Log.d(TAG, "Bearer Token HttpClient Request.");
                HttpGet httpGet = new HttpGet(ApiConstants.AUTHORIZATION_TOKEN_URL);

                httpGet.setHeader(ApiConstants.CONTENT_TYPE, ApiConstants.CONTENT_TYPE_FORMAT_TEXT_PLAIN + "," + ApiConstants.ACCEPT_TYPE_FORMAT_JSON);
                httpGet.setHeader(ApiConstants.ACCEPT_TYPE, ApiConstants.ACCEPT_TYPE_FORMAT_JSON + "," + ApiConstants.ACCEPT_TYPE_FORMAT_XML);
                httpGet.setHeader(ApiConstants.SESSION_TOKEN, sharedPreferences.getString(AnalyticsAppConstants.SESSION_TOKEN, ""));
                httpGet.setHeader(AnalyticsAppConstants.VIMOND_COOKIE, sharedPreferences.getString(AnalyticsAppConstants.VIMOND_COOKIE, ""));

                DefaultHttpClient httpClient = new DefaultHttpClient(httpGet.getParams());
                emitResponse(ApiConstants.AUTHORIZATION_TOKEN_URL, httpClient.execute(httpGet), emitter);
            } catch (Throwable t) {
                emitter.onError(t);
            }
        });
    }

    private void emitResponse(String url, HttpResponse httpResponse, ObservableEmitter<Token> emitter) throws IOException {

        /*200: BearerToken Success response code, 204: SessionToken Success response code*/
        if (httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK || httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT) {

            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) entity.getContent().close();

            Token token = new Token();
            token.code = httpResponse.getStatusLine().getStatusCode();
            token.headers = httpResponse.getAllHeaders();

            emitter.onNext(token);
            emitter.onComplete();
        } else {
            /*Create Throwable with the response & call emitter.onError(t)*/

            CustomHttpException customHttpException = new CustomHttpException(url, httpResponse);
            emitter.onError(customHttpException);
        }
    }


    @Override
    public Observable<String> getUUID() {
        return appAnalyticsDataService.getUUID();
    }

    @Override
    public Observable<String> getDeviceType() {
        return appAnalyticsDataService.getDeviceType();
    }

    @Override
    public Observable<String> getTrackingSessionID() {
        return appAnalyticsDataService.getTrackingSessionID();
    }

    @Override
    public Observable<String> getSubSessionID() {
        return appAnalyticsDataService.getSubSessionID();
    }

    @Override
    public Observable<String> getTimeZone() {

        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = timeZone.getOffset(now.getTime()) / 60000;

        return Observable.just(Integer.toString(offsetFromUtc).trim());

    }

    @Override
    public Observable<String> getUserAgent() {

        String UserAgent = AnalyticsAppConstants.DEVICE_INFO
                + AnalyticsAppConstants.OS_VERSION + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
                + AnalyticsAppConstants.OS_API_LEVEL + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")"
                + AnalyticsAppConstants.MANUFACTURER + Build.MANUFACTURER
                + AnalyticsAppConstants.MODEL_PRODUCT + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

        return Observable.just(UserAgent);
    }

    @Override
    public Observable<String> getIPAddress() {
        return appAnalyticsDataService.getIPAddress();
    }

    @Override
    public Observable<String> postEvent(AnalyticEvent analyticEvent) {

        analyticEvent.SubSessionId = sharedPreferences.getString(AnalyticsAppConstants.SUB_SESSION_ID, AnalyticsAppConstants.SUB_SESSION_ID);
        analyticEvent.TrackingSessionId = sharedPreferences.getString(AnalyticsAppConstants.TRACKING_SESSION_ID, AnalyticsAppConstants.TRACKING_SESSION_ID);
        analyticEvent.DeviceId = sharedPreferences.getString(AnalyticsAppConstants.DEVICE_ID, AnalyticsAppConstants.DEVICE_ID);
        analyticEvent.AppType = AnalyticsAppConstants.ANDROID + AnalyticsAppConstants.APP;
        analyticEvent.TimeStamp = String.valueOf(System.currentTimeMillis());
        analyticEvent.TimeZone = sharedPreferences.getString(AnalyticsAppConstants.TIMEZONE, Integer.toString(TimeZone.getDefault().getOffset(new Date().getTime()) / 60000));
        analyticEvent.UserAgent = sharedPreferences.getString(AnalyticsAppConstants.USERAGENT, AnalyticsAppConstants.USERAGENT);
        analyticEvent.IpAddress = sharedPreferences.getString(AnalyticsAppConstants.IP, AnalyticsAppConstants.IP);
        analyticEvent.AppId = AnalyticsAppConstants.WATCHABLE;
        analyticEvent.IsProduction = AnalyticsAppConstants.IS_PRODUCTION_ANALYTICS;
        analyticEvent.DeviceType = Build.MANUFACTURER;
        analyticEvent.ReportFrom = AnalyticsAppConstants.ANDROID + AnalyticsAppConstants.APP;
        analyticEvent.ZipCode = "-1";
        analyticEvent.UserId = "-1";
        if (analyticEvent.EventName.equalsIgnoreCase(AnalyticsAppConstants.APP_START))
            analyticEvent.Referral_code = "-1";

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse(ApiConstants.ACCEPT_TYPE_FORMAT_JSON + ";" + ApiConstants.CHARSET), new Gson().toJson(analyticEvent));
        Log.i(TAG, TAG + body.toString());
        return appAnalyticsDataService.postAnalyticEvent(body);
    }

}
