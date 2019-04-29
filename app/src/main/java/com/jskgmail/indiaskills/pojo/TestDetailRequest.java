package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TestDetailRequest implements Serializable{
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
    @SerializedName("languageCode")
    @Expose
    private String languageCode;

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

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
