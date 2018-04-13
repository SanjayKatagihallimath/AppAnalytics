package com.app.analytics.core.data.repository;

import android.content.SharedPreferences;
import android.util.Log;

import com.app.analytics.core.data.datasource.network.AppDataSourceNetworkImpl;
import com.app.analytics.core.domain.abstraction.AppDataRepository;
import com.app.analytics.core.domain.model.AnalyticEvent;
import com.app.analytics.utils.AppConstants;

import org.apache.http.Header;

import java.net.HttpURLConnection;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Sanjay.k on 4/1/2017.
 */

public class AppDataRepositoryImpl implements AppDataRepository {
    public static final String TAG = AppDataRepositoryImpl.class.getSimpleName();

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedPreferencesEditor;
    private final AppDataSourceNetworkImpl appDataSourceNetwork;

    public AppDataRepositoryImpl(SharedPreferences sharedPreferences, AppDataSourceNetworkImpl appDataSourceNetwork) {

        this.sharedPreferences = sharedPreferences;
        this.appDataSourceNetwork = appDataSourceNetwork;

        this.sharedPreferencesEditor = sharedPreferences.edit();
    }

    @Override
    public Observable<Response<Void>> getSessionToken() {

        return appDataSourceNetwork.getSessionToken();
    }

    @Override
    public Observable<Boolean> checkAndUpdateSessionToken() {

        if (isSessionTokenEmpty() || isSessionTokenTimeout()) {
            if (requestSessionToken() != null && this.sharedPreferences.getString(AppConstants.SESSION_TOKEN, "").trim().length() != 0) {
                return Observable.just(true);
            } else
                return Observable.just(false);
        } else {
            return Observable.just(true);
        }
    }

    @Override
    public Observable<Boolean> checkAndUpdateBearerToken() {
        return requestBearerToken();
    }

    private Boolean isSessionTokenEmpty() {

        return this.sharedPreferences.getString(AppConstants.SESSION_TOKEN, "").trim().length() == 0;
    }

    private Boolean isSessionTokenTimeout() {

        long currentTimeMillis = System.currentTimeMillis();
        long sessionTokenLastUpdateTime = this.sharedPreferences.getLong(AppConstants.SESSION_TOKEN_LAST_UPDATED, 0);

        if ((currentTimeMillis > sessionTokenLastUpdateTime) && ((currentTimeMillis - sessionTokenLastUpdateTime) > AppConstants.SESSION_TOKEN_EXPIRE_DURATION_IN_MILLIS)) {
            Log.d(TAG, "SessionTokenTimeOut: true");
            return true;
        }
        Log.d(TAG, "SessionTokenTimeOut: false");
        return false;
    }

    public Observable<String> requestSessionToken() {

        return appDataSourceNetwork.requestSessionToken().map(token -> {

            if (token.code == HttpURLConnection.HTTP_NO_CONTENT) {
                for (Header header : token.headers) {
                    if (header.getName().trim().equalsIgnoreCase(AppConstants.SESSION_TOKEN)) {

                        Log.d(TAG, "Session Token: " + header.getValue().trim());
                        sharedPreferencesEditor.putString(AppConstants.SESSION_TOKEN, header.getValue().trim());
                        sharedPreferencesEditor.putLong(AppConstants.SESSION_TOKEN_LAST_UPDATED, System.currentTimeMillis());
                        sharedPreferencesEditor.commit();
                        return header.getValue().trim();

                    }
                }
                return this.sharedPreferences.getString(AppConstants.SESSION_TOKEN, "").trim();
            }
            return null;
        });
    }

    private Observable<Boolean> requestBearerToken() {

        return appDataSourceNetwork.requestBearerToken().map(token -> {

            if (token.code == HttpURLConnection.HTTP_OK) {
                for (Header header : token.headers) {
                    if (header.getName().trim().equalsIgnoreCase(AppConstants.AUTHORIZATION)) {

                        Log.d(TAG, "Bearer Token: " + header.getValue().trim());
                        sharedPreferencesEditor.putString(AppConstants.AUTHORIZATION, header.getValue().trim());
                        sharedPreferencesEditor.commit();
                        break;
                    }
                }
                return true;
            }
            return false;
        });
    }


    @Override
    public Observable<String> getUUIDRepository() {
        return appDataSourceNetwork.getUUID();
    }

    @Override
    public Observable<String> getDeviceTypeRepository() {
        return appDataSourceNetwork.getDeviceType();
    }

    @Override
    public Observable<String> getTrackingSessionIDRepository() {
        return appDataSourceNetwork.getTrackingSessionID();
    }

    @Override
    public Observable<String> getSubSessionIDRepository() {
        return appDataSourceNetwork.getSubSessionID();
    }

    @Override
    public Observable<String> getTimeZoneRepository() {
        return appDataSourceNetwork.getTimeZone();
    }

    @Override
    public Observable<String> getIPAddressRepository() {
        return appDataSourceNetwork.getIPAddress();
    }

    @Override
    public Observable<String> getUserAgentRepository() {
        return appDataSourceNetwork.getUserAgent();
    }

    @Override
    public Observable<String> postEvent(AnalyticEvent analyticEvent) {
        return appDataSourceNetwork.postEvent(analyticEvent);
    }

}
