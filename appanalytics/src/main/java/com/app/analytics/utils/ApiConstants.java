package com.app.analytics.utils;

/**
 * Created by Sanjay.k on 1/18/2017.
 */

public class ApiConstants {

    public static final String APPLICATION_NAME = "watchable";
    public static final String PLATFORM = "/iptv";
    public static final String FMDS_API = "fmds/api/";

    public static final String ACCEPT_TYPE = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String USER_AGENT = "user-agent";
    public static final String USER_SESSION_ID = "User-Session-Id";
    public static final String SESSION_TOKEN = "SessionToken";
    public static final String FABRIC_DEVICE_ID = "Fabric-Device-Id";
    public static final String PAGE_SIZE = "pageSize";
    public static final String PAGE_NUM = "pageNum";

    public static final String ACCEPT_TYPE_FORMAT_JSON = "application/json";
    public static final String ACCEPT_TYPE_FORMAT_XML = "application/xml";
    public static final String ACCEPT_TYPE_FORMAT_V4_JSON = "application/vnd.fmds.v4+json";
    public static final String CONTENT_TYPE_FORMAT_TEXT_PLAIN = "text/plain";
    public static final String CHARSET = "charset=utf-8";

    //.................
    /*Demo3 Base Url*/
    public static final String DEMO3_BASE_URL = "fabricdemo.xidio.com/";
    /*ZINC Base Url*/
    public static final String ZNIC_BASE_URL = "zinc.demo.xidio.net/";
    /*Fabric Group Base Url*/
    public static final String FABRIC_GROUP_BASE_URL = "fabricgroup.xidio.com/";
    /*Fabric Hybrid Base Url...Bit.ly*/
    public static final String FABRIC_HYBRID_BASE_URL = "fabrichybrid.xidio.com/";
    /*Fabric Fas Base Url...Analytics*/
    public static final String FABRIC_FAS_BASE_URL = "fas.xidio.com/";
    /*Production Base Url*/
    public static final String PRODUCTION_BASE_URL = "fabric.xidio.com/";
    /* FUUIDS  Base url */
    public static final String FUUIDS_BASE_URL = "fuuids.xidio.com/";
    /* FUUIDS  Base url */
    public static final String IP_ADDRESS_BASE_URL = "demoanalytics.xidio.com:1443";


    public static final String BASE_PRODUCTION_URL_HTTP = "http://" + PRODUCTION_BASE_URL;
    public static final String BASE_URL_HTTP = "http://" + FABRIC_GROUP_BASE_URL;/*......1. Replace the Base Url's here in case need to point Demo3/Zinc/Fabric group/Production......*/
    public static final String BASE_URL_HTTPS = "https://" + FABRIC_GROUP_BASE_URL;/*......2. Replace the Base Url's here in case need to point Demo3/Zinc/Fabric group/Production......*/
    public static final String BASE_HYBRID_URL = "http://" + FABRIC_HYBRID_BASE_URL;
    public static final String BASE_FUUIDS_URL = "http://" + FUUIDS_BASE_URL;
    public static final String BASE_IP_ADDRESS_URL = "https://" + IP_ADDRESS_BASE_URL;
    public static final String BASE_FABRIC_FAS_BASE_URL = "http://" + FABRIC_FAS_BASE_URL;

    public static final String FMDS_APP_PLATFORM = FMDS_API + APPLICATION_NAME + PLATFORM;

    /*Authentication Apis*/
    public static final String SESSION_TOKEN_SUB_URL = FMDS_APP_PLATFORM + "/authenticate";
    public static final String SESSION_TOKEN_URL = BASE_URL_HTTP + SESSION_TOKEN_SUB_URL;
    public static final String AUTHORIZATION_TOKEN_SUB_URL = FMDS_APP_PLATFORM + "/users/profile";
    public static final String AUTHORIZATION_TOKEN_URL = BASE_URL_HTTP + AUTHORIZATION_TOKEN_SUB_URL;


    public static final String UUID = "/fuuids/api/uuid";
    public static final String DEVICE_ID_URL = "/fuuids/api/uuid/device";
    public static final String TRACKING_SESSION_ID_URL = "/fuuids/api/uuid/session";
    public static final String SUB_SESSION_ID_URL = "/fuuids/api/uuid/session/sub";
    public static final String IP_ADDRESS = "/json/";

    public static final String ANALYTIC_EVENT = "/fas/api/event";

}
