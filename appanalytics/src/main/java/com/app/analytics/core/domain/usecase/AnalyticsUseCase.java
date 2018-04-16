package com.app.analytics.core.domain.usecase;


import android.util.Log;

import com.app.analytics.core.base.BaseUseCase;
import com.app.analytics.core.data.repository.AppDataRepositoryImpl;
import com.app.analytics.core.domain.abstraction.AppDataRepository;
import com.app.analytics.core.domain.model.AnalyticsIDType;

import io.reactivex.Observable;

/**
 * Created by sanjay.k on 31-Jul-17.
 */

public class AnalyticsUseCase extends BaseUseCase<String> {

    public static final String TAG = AnalyticsUseCase.class.getSimpleName();

    private final AppDataRepositoryImpl appDataRepositoryImpl;


    public AnalyticsUseCase(AppDataRepositoryImpl appDataRepositoryImpl) {
        super(appDataRepositoryImpl);
        this.appDataRepositoryImpl = appDataRepositoryImpl;
    }

    @Override
    protected Observable<String> createObservable() {
        return null;
    }

    @Override
    protected Observable<String> createObservable(Object objectKey) {


        AnalyticsIDType analyticsIDType = (AnalyticsIDType) objectKey;
        Log.i(TAG, TAG + analyticsIDType + ": " + analyticsIDType);
        switch (analyticsIDType) {
            case SESSION_TOKEN:
                return appDataRepositoryImpl.requestSessionToken();
            case UUID:
                return appDataRepositoryImpl.getUUIDRepository();

            case DEVICE_ID:
                return appDataRepositoryImpl.getDeviceTypeRepository();

            case TRACKING_SESSION_ID:
                return appDataRepositoryImpl.getTrackingSessionIDRepository();

            case SUB_SESSION_ID:
                return appDataRepositoryImpl.getSubSessionIDRepository();

            case TIMEZONE:
                return appDataRepositoryImpl.getTimeZoneRepository();

            case USERAGENT:
                return appDataRepositoryImpl.getUserAgentRepository();

            case IP_ADDRESS:
                return appDataRepositoryImpl.getIPAddressRepository();


        }

        return null;
    }
}
