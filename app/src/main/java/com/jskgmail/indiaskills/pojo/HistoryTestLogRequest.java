package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryTestLogRequest {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("api_key")
    @Expose
    private String apiKey;
    @SerializedName("schedule_unique_key")
    @Expose
    private String scheduleUniqueKey;
    @SerializedName("log")
    @Expose
    private Logs log;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getScheduleUniqueKey() {
        return scheduleUniqueKey;
    }

    public void setScheduleUniqueKey(String scheduleUniqueKey) {
        this.scheduleUniqueKey = scheduleUniqueKey;
    }

    public Logs getLog() {
        return log;
    }

    public void setLog(Logs log) {
        this.log = log;
    }

}
