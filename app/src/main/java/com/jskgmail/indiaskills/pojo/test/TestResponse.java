package com.jskgmail.indiaskills.pojo.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestResponse {
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private TestData data;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TestData getData() {
        return data;
    }

    public void setData(TestData data) {
        this.data = data;
    }
}
