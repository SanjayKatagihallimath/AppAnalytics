package com.app.analytics.core.data.datasource.network;


import com.app.analytics.utils.ApiConstants;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Sanjay.k on 4/1/2017.
 */

public interface AppDataService {

    @Headers({ApiConstants.ACCEPT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON,
            ApiConstants.CONTENT_TYPE + ": " + ApiConstants.CONTENT_TYPE_FORMAT_TEXT_PLAIN})
    @POST(ApiConstants.SESSION_TOKEN_SUB_URL)
    Observable<Response<Void>> getSessionToken(@Body String authToken);

    @Headers({ApiConstants.ACCEPT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON,
            ApiConstants.CONTENT_TYPE + ": " + ApiConstants.CONTENT_TYPE_FORMAT_TEXT_PLAIN})
    @POST(ApiConstants.SESSION_TOKEN_SUB_URL)
    Call<Response<Void>> getSessionTokenCall(@Body String authToken);

    @Headers({ApiConstants.ACCEPT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON,
            ApiConstants.CONTENT_TYPE + ": " + ApiConstants.CONTENT_TYPE_FORMAT_TEXT_PLAIN})
    @POST(ApiConstants.SESSION_TOKEN_SUB_URL)
    Call<Response<Void>> getSessionTokenRequestBody(@Body RequestBody authToken);

    @Headers({ApiConstants.ACCEPT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_V4_JSON,
            ApiConstants.CONTENT_TYPE + ": " + ApiConstants.CONTENT_TYPE_FORMAT_TEXT_PLAIN})
    @HTTP(method = "POST", path = ApiConstants.SESSION_TOKEN_SUB_URL, hasBody = true)
    Observable<Response<ResponseBody>> getSessionTokenCustom(@Body String authToken);


    @Headers(ApiConstants.CONTENT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON)
    @GET(ApiConstants.UUID)
    Observable<String> getUUID();

    @Headers(ApiConstants.CONTENT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON)
    @GET(ApiConstants.DEVICE_ID_URL)
    Observable<String> getDeviceType();

    @Headers(ApiConstants.CONTENT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON)
    @GET(ApiConstants.TRACKING_SESSION_ID_URL)
    Observable<String> getTrackingSessionID();

    @Headers(ApiConstants.CONTENT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON)
    @GET(ApiConstants.SUB_SESSION_ID_URL)
    Observable<String> getSubSessionID();

    @Headers(ApiConstants.CONTENT_TYPE + ": " + ApiConstants.ACCEPT_TYPE_FORMAT_JSON)
    @GET(ApiConstants.IP_ADDRESS)
    Observable<String> getIPAddress();

    @POST(ApiConstants.ANALYTIC_EVENT)
    Observable<String> postAnalyticEvent(@Body RequestBody eventBody);

}
