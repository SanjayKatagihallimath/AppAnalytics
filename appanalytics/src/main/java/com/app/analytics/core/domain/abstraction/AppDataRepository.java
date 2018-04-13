package com.app.analytics.core.domain.abstraction;

import com.app.analytics.core.domain.model.AnalyticEvent;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Sanjay.k on 4/1/2017.
 */

public interface AppDataRepository {

    Observable<Response<Void>> getSessionToken();

    Observable<Boolean> checkAndUpdateSessionToken();

    Observable<Boolean> checkAndUpdateBearerToken();

    Observable<String> getUUIDRepository();

    Observable<String> getDeviceTypeRepository();

    Observable<String> getTrackingSessionIDRepository();

    Observable<String> getSubSessionIDRepository();

    Observable<String> getTimeZoneRepository();

    Observable<String> getIPAddressRepository();

    Observable<String> getUserAgentRepository();

    Observable<String> postEvent(AnalyticEvent analyticEvent);

}
