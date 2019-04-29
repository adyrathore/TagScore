package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TestUploadRequest implements Serializable{
    @SerializedName("api_key")
    @Expose
    private String apiKey;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("testID")
    @Expose
    private String testID;
    @SerializedName("uniqueId")
    @Expose
    private String uniqueId;
    @SerializedName("schedule_id")
    @Expose
    private String scheduleId;
    @SerializedName("test_data")
    @Expose
    private String testData;

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

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }
}
