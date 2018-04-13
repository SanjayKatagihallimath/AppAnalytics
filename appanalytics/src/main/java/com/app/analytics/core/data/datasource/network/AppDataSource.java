package com.app.analytics.core.data.datasource.network;


import com.app.analytics.core.domain.model.AnalyticEvent;
import com.app.analytics.core.domain.model.Token;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Sanjay.k on 4/1/2017.
 */

public interface AppDataSource {

    Observable<Response<Void>> getSessionToken();

    Observable<Token> requestSessionToken();

    Observable<Token> requestBearerToken();

    Observable<String> getUUID();

    Observable<String> getDeviceType();

    Observable<String> getTrackingSessionID();

    Observable<String> getSubSessionID();

    Observable<String> getTimeZone();

    Observable<String> getUserAgent();

    Observable<String> getIPAddress();

    Observable<String> postEvent(AnalyticEvent analyticEvent);

}
