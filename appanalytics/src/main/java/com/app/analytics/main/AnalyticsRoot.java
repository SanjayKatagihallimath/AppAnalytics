package com.app.analytics.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.analytics.core.data.datasource.network.AppDataService;
import com.app.analytics.core.data.datasource.network.AppDataSourceNetworkImpl;
import com.app.analytics.core.data.repository.AppDataRepositoryImpl;
import com.app.analytics.core.domain.model.AnalyticEvent;
import com.app.analytics.core.domain.model.AnalyticsIDType;
import com.app.analytics.core.domain.usecase.AnalyticsEventsUseCase;
import com.app.analytics.core.domain.usecase.AnalyticsUseCase;
import com.app.analytics.utils.AnalyticsAppConstants;
import com.app.analytics.utils.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Retrofit;

public class AnalyticsRoot implements AnalyticsInfo {

    public static final String TAG = AnalyticsRoot.class.getSimpleName();

    private Context context;
    private Retrofit retrofit, retrofitAnalytics;
    private SharedPreferences sharedPreferences;
    private AnalyticsUseCase analyticsUseCase;
    private AnalyticsEventsUseCase analyticsEventsUseCase;
    private AppDataRepositoryImpl appDataRepositoryImpl;
    private AppDataSourceNetworkImpl appDataSourceNetworkImpl;
    private AppDataService appDataService, appAnalyticsDataService;

    private SharedPreferences.Editor sharedPreferencesEditor;

    public AnalyticsRoot(Context context) {
        this.context = context;
        init();
    }


    private void init() {

        sharedPreferences = context.getApplicationContext().getSharedPreferences(AnalyticsAppConstants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        this.sharedPreferencesEditor = sharedPreferences.edit();

        retrofit = new ServiceUtil().retrofitProductionHTTP(context);
        retrofitAnalytics = new ServiceUtil().retrofitFUUIDS(context);

        appDataService = retrofit.create(AppDataService.class);
        appAnalyticsDataService = retrofitAnalytics.create(AppDataService.class);
        appDataSourceNetworkImpl = new AppDataSourceNetworkImpl(appDataService, appAnalyticsDataService, sharedPreferences);
        appDataRepositoryImpl = new AppDataRepositoryImpl(sharedPreferences, appDataSourceNetworkImpl);
        analyticsUseCase = new AnalyticsUseCase(appDataRepositoryImpl);
        analyticsEventsUseCase = new AnalyticsEventsUseCase(appDataRepositoryImpl);
    }


    public void initData() {
        sharedPreferencesEditor.putString(AnalyticsAppConstants.IP, "-1");
        sharedPreferencesEditor.apply();
        //analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.SESSION_TOKEN), AnalyticsIDType.SESSION_TOKEN);
        analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.UUID), AnalyticsIDType.UUID);
        analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.DEVICE_ID), AnalyticsIDType.DEVICE_ID);
        analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.TRACKING_SESSION_ID), AnalyticsIDType.TRACKING_SESSION_ID);
        analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.SUB_SESSION_ID), AnalyticsIDType.SUB_SESSION_ID);
        analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.TIMEZONE), AnalyticsIDType.TIMEZONE);
        analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.IP_ADDRESS), AnalyticsIDType.IP_ADDRESS);
        analyticsUseCase.executeWithObserver(new AppObserver(AnalyticsIDType.USERAGENT), AnalyticsIDType.USERAGENT);
    }


    @Override
    public String getSessionToken() {
        return sharedPreferences.getString(AnalyticsAppConstants.SESSION_TOKEN, "");
    }

    @Override
    public String getUUIDRepository() {
        return sharedPreferences.getString(AnalyticsAppConstants.UUID, "");
    }

    @Override
    public String getDeviceTypeRepository() {
        return sharedPreferences.getString(AnalyticsAppConstants.DEVICE_ID, "");
    }

    @Override
    public String getTrackingSessionIDRepository() {
        return sharedPreferences.getString(AnalyticsAppConstants.TRACKING_SESSION_ID, "");
    }

    @Override
    public String getSubSessionIDRepository() {
        return sharedPreferences.getString(AnalyticsAppConstants.SUB_SESSION_ID, "");
    }

    @Override
    public String getTimeZoneRepository() {
        return sharedPreferences.getString(AnalyticsAppConstants.TIMEZONE, "");
    }

    @Override
    public String getIPAddressRepository() {
        return sharedPreferences.getString(AnalyticsAppConstants.IP_ADDRESS, "");
    }

    @Override
    public String getUserAgentRepository() {
        return sharedPreferences.getString(AnalyticsAppConstants.USERAGENT, "");
    }

    @Override
    public void postEvent(AnalyticEvent analyticEvent) {
        analyticsEventsUseCase.execute(new EventObserver(analyticEvent.EventName), analyticEvent);
    }


    class AppObserver extends DisposableObserver {
        private AnalyticsIDType analyticsIDType;

        public AppObserver(AnalyticsIDType analyticsIDType) {
            this.analyticsIDType = analyticsIDType;
        }

        @Override
        public void onNext(Object object) {

            Log.i(TAG, TAG + analyticsIDType + ": " + object);


            switch (analyticsIDType) {

                case UUID:
                    if (object instanceof String) {
                        sharedPreferencesEditor.putString(AnalyticsAppConstants.UUID, ((String) object).trim());
                        sharedPreferencesEditor.apply();
                    }
                    break;
                case DEVICE_ID:
                    if (object instanceof String) {
                        sharedPreferencesEditor.putString(AnalyticsAppConstants.DEVICE_ID, ((String) object).trim());
                        sharedPreferencesEditor.apply();
                    }
                    break;
                case TRACKING_SESSION_ID:
                    if (object instanceof String) {
                        sharedPreferencesEditor.putString(AnalyticsAppConstants.TRACKING_SESSION_ID, ((String) object).trim());
                        sharedPreferencesEditor.putLong(AnalyticsAppConstants.TRACKING_SESSION_ID_LAST_UPDATED, System.currentTimeMillis());
                        sharedPreferencesEditor.apply();
                    }
                    break;
                case SUB_SESSION_ID:
                    if (object instanceof String) {
                        sharedPreferencesEditor.putString(AnalyticsAppConstants.SUB_SESSION_ID, ((String) object).trim());
                        sharedPreferencesEditor.apply();
                    }
                    break;
                case TIMEZONE:
                    if (object instanceof String) {
                        sharedPreferencesEditor.putString(AnalyticsAppConstants.TIMEZONE, ((String) object).trim());
                        sharedPreferencesEditor.apply();
                    }
                    break;
                case USERAGENT:
                    if (object instanceof String) {
                        sharedPreferencesEditor.putString(AnalyticsAppConstants.USERAGENT, ((String) object).trim());
                        sharedPreferencesEditor.apply();
                    }
                    break;
                case IP_ADDRESS:
                    try {
                        if (object instanceof String) {
                            JSONObject jsonObject = new JSONObject(((String) object).trim());
                            sharedPreferencesEditor.putString(AnalyticsAppConstants.IP, jsonObject.getString(AnalyticsAppConstants.IP));
                            sharedPreferencesEditor.apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, TAG + analyticsIDType + ": " + e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    }

    class EventObserver extends DisposableObserver<String> {

        private String eventName;

        public EventObserver(String eventName) {
            this.eventName = eventName;
        }

        @Override
        public void onNext(String value) {

            Log.i(TAG, TAG + eventName + value);
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, TAG + eventName + e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    }

}
