package com.jskgmail.indiaskills.pojo;

/**
 * Created by aditya.singh on 12/12/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseLogin implements Serializable {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("api_key")
    @Expose
    private String apiKey;
    @SerializedName("profile")
    @Expose
    private List<LoginProfile> profile = null;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<LoginProfile> getProfile() {
        return profile;
    }

    public void setProfile(List<LoginProfile> profile) {
        this.profile = profile;
    }

}
