package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TestFeedbackRequest implements Serializable{
    @SerializedName("api_key")
    @Expose
    private String apiKey;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("schedule_unique_key")
    @Expose
    private String scheduleUniqueKey;
    @SerializedName("feedback")
    @Expose
    private String feedback;


    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScheduleUniqueKey() {
        return scheduleUniqueKey;
    }

    public void setScheduleUniqueKey(String scheduleUniqueKey) {
        this.scheduleUniqueKey = scheduleUniqueKey;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
