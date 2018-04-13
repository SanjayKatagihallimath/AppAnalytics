package com.app.analytics.main;

import com.app.analytics.core.domain.model.AnalyticEvent;

public interface AnalyticsInfo {


    String getSessionToken();

    String getBearerToken();

    String getUUIDRepository();

    String getDeviceTypeRepository();

    String getTrackingSessionIDRepository();

    String getSubSessionIDRepository();

    String getTimeZoneRepository();

    String getIPAddressRepository();

    String getUserAgentRepository();

    String postEvent(AnalyticEvent analyticEvent);

}
