package com.app.analytics.core.domain.model;

import com.app.analytics.utils.AnalyticsAppConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sanjay.k on 01-Aug-17.
 */

public class AnalyticEvent {

    @SerializedName("EventType")
    @Expose
    public String EventType;

    @SerializedName("EventName")
    @Expose
    public String EventName;

    @SerializedName("PlaylistId")
    @Expose
    public String PlaylistId;

    @SerializedName("ChannelId")
    @Expose
    public String ChannelId;

    @SerializedName("AssetId")
    @Expose
    public String AssetId;

    @SerializedName("PreviousVideoId")
    @Expose
    public String PreviousVideoId;

    @SerializedName("PerceivedBandwidth")
    @Expose
    public String PerceivedBandwidth;

    @SerializedName("ProgressMarker")
    @Expose
    public String ProgressMarker;

    @SerializedName(AnalyticsAppConstants.SUB_SESSION_ID)
    @Expose
    public String SubSessionId;

    @SerializedName(AnalyticsAppConstants.TRACKING_SESSION_ID)
    @Expose
    public String TrackingSessionId;

    @SerializedName("Referral_code")
    @Expose
    public String Referral_code;

    @SerializedName(AnalyticsAppConstants.DEVICE_ID)
    @Expose
    public String DeviceId;

    @SerializedName("UserId")
    @Expose
    public String UserId;

    @SerializedName("AppType")
    @Expose
    public String AppType;

    @SerializedName("TimeStamp")
    @Expose
    public String TimeStamp;

    @SerializedName(AnalyticsAppConstants.TIMEZONE)
    @Expose
    public String TimeZone;

    @SerializedName("UserAgent")
    @Expose
    public String UserAgent;

    @SerializedName(AnalyticsAppConstants.IP_ADDRESS)
    @Expose
    public String IpAddress;

    @SerializedName("AppId")
    @Expose
    public String AppId;

    @SerializedName("IsProduction")
    @Expose
    public String IsProduction;

    @SerializedName("DeviceType")
    @Expose
    public String DeviceType;

    @SerializedName("ReportFrom")
    @Expose
    public String ReportFrom;

    @SerializedName("ZipCode")
    @Expose
    public String ZipCode;


    public AnalyticEvent() {

    }

    public AnalyticEvent(String EventType, String EventName, String PlaylistId, String ChannelId, String AssetId,
                         String PreviousVideoId, String PerceivedBandwidth, String ProgressMarker, String SubSessionId,
                         String TrackingSessionId, String Referral_code, String DeviceId, String UserId,
                         String AppType, String TimeStamp, String TimeZone, String UserAgent, String IpAddress,
                         String AppId, String IsProduction, String DeviceType, String ReportFrom,
                         String ZipCode) {


        this.EventType = EventType;
        this.EventName = EventName;
        this.PlaylistId = PlaylistId;
        this.ChannelId = ChannelId;
        this.AssetId = AssetId;
        this.PreviousVideoId = PreviousVideoId;
        this.PerceivedBandwidth = PerceivedBandwidth;
        this.ProgressMarker = ProgressMarker;
        this.SubSessionId = SubSessionId;
        this.TrackingSessionId = TrackingSessionId;
        this.Referral_code = Referral_code;
        this.DeviceId = DeviceId;
        this.UserId = UserId;
        this.AppType = AppType;
        this.TimeStamp = TimeStamp;
        this.TimeZone = TimeZone;
        this.UserAgent = UserAgent;
        this.IpAddress = IpAddress;
        this.AppId = AppId;
        this.IsProduction = IsProduction;
        this.DeviceType = DeviceType;
        this.ReportFrom = ReportFrom;
        this.ZipCode = ZipCode;


    }

}
