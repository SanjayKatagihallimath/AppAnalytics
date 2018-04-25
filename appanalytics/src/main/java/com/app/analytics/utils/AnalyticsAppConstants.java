package com.app.analytics.utils;

public class AnalyticsAppConstants {

    /*Integer Constants*/
    public static final Integer cache_size = 10 * 1000 * 1000; // 10 MB
    public static final Integer ok_http_timeout = 30; // 30 Sec


    /*String Constants*/
    public static final String ok_http_cache = "ok_http_cache";

    public static final String authToken = "9a5901f0-63f9-4938-87cc-7a9b733788c1_3a5a0a7f-3e23-4fb2-9e16-4df426cf2190";


    public static final String SEARCH_STRING = "searchString";
    public static final String PREFERENCES_FILE = "AnalyticsAppPreferences";

    /*Session Token Preferences Constants*/
    public static final String SESSION_TOKEN = "SessionToken";
    public static final String SESSION_TOKEN_LAST_UPDATED = "SessionTokenLastUpdated";
    public static final long SESSION_TOKEN_EXPIRE_DURATION_IN_MILLIS = 1799000;

    /*Ananlytics Preferences Constants*/
    public static final String DEVICE_ID = "DeviceId";
    public static final String TRACKING_SESSION_ID = "TrackingSessionId";
    public static final String TRACKING_SESSION_ID_LAST_UPDATED = "TrackingSessionLastUpdated";
    public static final long TRACKING_SESSION_ID_EXPIRE_DURATION_IN_MILLIS = 1799000;
    public static final String SUB_SESSION_ID = "SubSessionId";
    public static final String TIMEZONE = "TimeZone";
    public static final String IP_ADDRESS = "IpAddress";
    public static final String IP = "ip";
    public static final String USERAGENT = "UserAgent";
    public static final String WAS_BACKGROUND = "wasBackground";
    public static final String APP_START_EVENT_FIRED = "appStartEventFired";

    /*Authorization Fail type constants*/
    public static final String USER = "USER";
    public static final String FABRIC = "FABRIC";

    /*Flambe Analytics Constants*/
    public static final String UUID = "uuid";
    public static final String UUID_DEFAULT_VALUE = "-1";

    // Device details
    public static final String DEVICE_INFO = " Device-info: ";
    public static final String OS_VERSION = " OS Version: ";
    public static final String OS_API_LEVEL = " OS API Level: ";
    public static final String MANUFACTURER = " MANUFACTURER: ";
    public static final String MODEL_PRODUCT = " Model (and Product): ";


    /*User Preferences Request constants*/
    public static final String USERNAME_CAMEL_CASE = "userName";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String REMEMBER_ME = "rememberMe";
    public static final String EMAIL = "email";
    public static final String EMAIL_STATUS = "emailStatus";
    public static final String MOBILE_STATUS = "mobileStatus";
    public static final String REGD_DATE = "regdDate";
    public static final String CONFIRM_EMAIL = "confirmEmail";
    public static final String NOTIFY_USER_ON_CREATION = "notifyUserOnCreation";
    public static final int CONFIRM_EMAIL_CONSTANT = 1;
    public static final String SHARE_LINK = "share_link";


    /*User Preferences Response constants*/
    public static final String AUTHORIZATION = "Authorization";
    public static final String VIMOND_COOKIE = "Vimond-Cookie";
    public static final String USER_ID = "UserId";



    public static final String SHARE_SHOWS = "shows";
    public static final String SHARE_VIDEOS = "videos";


    public static final String EVENT_NAME = "EventName";
    public static final int ANALYTICS_HEARTBEAT_OFFSET = 60000;
    // Analytics Events Name

    public static final String IS_PRODUCTION_ANALYTICS = "no";
    public static final String ANDROID = "Android";
    public static final String APP = " App";
    public static final String WATCHABLE = "Watchable";
    public static final String LIFE_CYCLE = "LifeCycle";
    public static final String HEARTBEAT = "HeartBeat";
    public static final String PLAYER = "Player";
    public static final String AD = "Ad";
    public static final String ERROR = "Error";


    public static final String EVENT_HEARTBEAT = "heartBeat";
    public static final String APP_START = "appStart";
    public static final String APP_END = "appEnd";
    public static final String USER_ACTION = "UserAction";
    public static final String SPLASH = "Splash";
    public static final String SIGNUP = "SignUp";
    public static final String SIGNIN = "SignIn";
    public static final String LOGIN = "Login";
    public static final String LOGOUT = "Logout";
    public static final String FORGOT_PASSWORD = "ForgotPassword";
    public static final String SEARCH = "Search";
    public static final String SHARE_PLAYLIST_OR_VIDEO = "sharePlaylistOrVideo";
    public static final String SHARE_SOCIAL_TYPE = "shareSocialType";
    public static final String EMAIL_SHARE_PLAYLIST = "emailSharePlaylist";
    public static final String EMAIL_SHARE_VIDEO = "emailShareVideo";
    public static final String PLAYLIST_ID = "PlaylistId";
    public static final String PLAYLIST_TITLE = "playlistTitle";
    public static final String COPY_URL_SHARE = "copyUrlShare";
    public static final String REFERRAL_CODE = "Referral_code";
    public static final String START = "start";
    public static final String PLAY = "play";
    public static final String REWIND = "rewind";
    public static final String END = "end";
    public static final String PROGRESS = "progress";
    public static final String PLAY_3SEC = "play_3sec";
    public static final String PLAY_5SEC = "play_5sec";
    public static final String ERROR_EVENT = "error";
    public static final String AD_START = "adStart";
    public static final String AD_END = "adEnd";
}
