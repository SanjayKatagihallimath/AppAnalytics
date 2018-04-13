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
import com.app.analytics.utils.AppConstants;
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

        retrofit = new ServiceUtil().retrofitProductionHTTP(context);
        retrofitAnalytics = new ServiceUtil().retrofitFUUIDS(context);
        sharedPreferences = context.getApplicationContext().getSharedPreferences(AppConstants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        this.sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(AppConstants.IP, "-1");
        sharedPreferencesEditor.apply();
        appDataService = retrofit.create(AppDataService.class);
        appAnalyticsDataService = retrofitAnalytics.create(AppDataService.class);
        appDataSourceNetworkImpl = new AppDataSourceNetworkImpl(appDataService, appAnalyticsDataService, sharedPreferences);
        appDataRepositoryImpl = new AppDataRepositoryImpl(sharedPreferences, appDataSourceNetworkImpl);
        analyticsUseCase = new AnalyticsUseCase(appDataRepositoryImpl);
        analyticsEventsUseCase = new AnalyticsEventsUseCase(appDataRepositoryImpl);
    }


    public void initData() {

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
        return sharedPreferences.getString(AppConstants.SESSION_TOKEN, "");
    }

    @Override
    public String getBearerToken() {
        return null;
    }

    @Override
    public String getUUIDRepository() {
        return sharedPreferences.getString(AppConstants.UUID, "");
    }

    @Override
    public String getDeviceTypeRepository() {
        return sharedPreferences.getString(AppConstants.DEVICE_ID, "");
    }

    @Override
    public String getTrackingSessionIDRepository() {
        return sharedPreferences.getString(AppConstants.TRACKING_SESSION_ID, "");
    }

    @Override
    public String getSubSessionIDRepository() {
        return sharedPreferences.getString(AppConstants.SUB_SESSION_ID, "");
    }

    @Override
    public String getTimeZoneRepository() {
        return sharedPreferences.getString(AppConstants.TIMEZONE, "");
    }

    @Override
    public String getIPAddressRepository() {
        return sharedPreferences.getString(AppConstants.IP_ADDRESS, "");
    }

    @Override
    public String getUserAgentRepository() {
        return sharedPreferences.getString(AppConstants.USERAGENT, "");
    }

    @Override
    public String postEvent(AnalyticEvent analyticEvent) {
        return analyticsEventsUseCase.execute(analyticEvent).blockingSingle();
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

                    sharedPreferencesEditor.putString(AppConstants.UUID, ((String) object).trim());
                    sharedPreferencesEditor.apply();
                    break;
                case DEVICE_ID:

                    sharedPreferencesEditor.putString(AppConstants.DEVICE_ID, ((String) object).trim());
                    sharedPreferencesEditor.apply();
                    break;
                case TRACKING_SESSION_ID:

                    sharedPreferencesEditor.putString(AppConstants.TRACKING_SESSION_ID, ((String) object).trim());
                    sharedPreferencesEditor.putLong(AppConstants.TRACKING_SESSION_ID_LAST_UPDATED, System.currentTimeMillis());
                    sharedPreferencesEditor.apply();
                    break;
                case SUB_SESSION_ID:

                    sharedPreferencesEditor.putString(AppConstants.SUB_SESSION_ID, ((String) object).trim());
                    sharedPreferencesEditor.apply();
                    break;
                case TIMEZONE:

                    sharedPreferencesEditor.putString(AppConstants.TIMEZONE, ((String) object).trim());
                    sharedPreferencesEditor.apply();
                    break;
                case USERAGENT:

                    sharedPreferencesEditor.putString(AppConstants.USERAGENT, ((String) object).trim());
                    sharedPreferencesEditor.apply();
                    break;
                case IP_ADDRESS:
                    try {
                        JSONObject jsonObject = new JSONObject(((String) object).trim());
                        sharedPreferencesEditor.putString(AppConstants.IP, jsonObject.getString(AppConstants.IP));
                        sharedPreferencesEditor.apply();
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

}
