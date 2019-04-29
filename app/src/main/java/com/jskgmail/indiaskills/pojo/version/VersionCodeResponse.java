package com.jskgmail.indiaskills.pojo.version;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionCodeResponse {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("version_code")
    @Expose
    private String versionCode;
    @SerializedName("version")
    @Expose
    private String version;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
