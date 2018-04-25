package com.app.analytics.core.data.repository;

import android.content.SharedPreferences;
import android.util.Log;

import com.app.analytics.core.data.datasource.network.AppDataSourceNetworkImpl;
import com.app.analytics.core.domain.abstraction.AppDataRepository;
import com.app.analytics.core.domain.model.AnalyticEvent;
import com.app.analytics.core.domain.model.Token;
import com.app.analytics.utils.AnalyticsAppConstants;

import org.apache.http.Header;

import java.net.HttpURLConnection;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
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

            return requestSessionToken();

        } else {
            return Observable.just(true);
        }
    }

    @Override
    public Observable<Boolean> checkAndUpdateBearerToken() {
        return requestBearerToken();
    }

    private Boolean isSessionTokenEmpty() {

        return this.sharedPreferences.getString(AnalyticsAppConstants.SESSION_TOKEN, "").trim().length() == 0;
    }

    private Boolean isSessionTokenTimeout() {

        long currentTimeMillis = System.currentTimeMillis();
        long sessionTokenLastUpdateTime = this.sharedPreferences.getLong(AnalyticsAppConstants.SESSION_TOKEN_LAST_UPDATED, 0);

        if ((currentTimeMillis > sessionTokenLastUpdateTime) && ((currentTimeMillis - sessionTokenLastUpdateTime) > AnalyticsAppConstants.SESSION_TOKEN_EXPIRE_DURATION_IN_MILLIS)) {
            Log.d(TAG, "SessionTokenTimeOut: true");
            return true;
        }
        Log.d(TAG, "SessionTokenTimeOut: false");
        return false;
    }

    public Observable<Boolean> requestSessionToken() {

        return appDataSourceNetwork.requestSessionToken().map(new Function<Token, Boolean>() {
            @Override
            public Boolean apply(Token token) throws Exception {

                if (token.code == HttpURLConnection.HTTP_NO_CONTENT) {
                    for (Header header : token.headers) {
                        if (header.getName().trim().equalsIgnoreCase(AnalyticsAppConstants.SESSION_TOKEN)) {

                            Log.d(TAG, "Session Token: " + header.getValue().trim());
                            sharedPreferencesEditor.putString(AnalyticsAppConstants.SESSION_TOKEN, header.getValue().trim());
                            sharedPreferencesEditor.putLong(AnalyticsAppConstants.SESSION_TOKEN_LAST_UPDATED, System.currentTimeMillis());
                            sharedPreferencesEditor.commit();
                            break;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private Observable<Boolean> requestBearerToken() {

        return appDataSourceNetwork.requestBearerToken().map(token -> {

            if (token.code == HttpURLConnection.HTTP_OK) {
                for (Header header : token.headers) {
                    if (header.getName().trim().equalsIgnoreCase(AnalyticsAppConstants.AUTHORIZATION)) {

                        Log.d(TAG, "Bearer Token: " + header.getValue().trim());
                        sharedPreferencesEditor.putString(AnalyticsAppConstants.AUTHORIZATION, header.getValue().trim());
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
