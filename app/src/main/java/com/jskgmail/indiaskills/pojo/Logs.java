package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Logs {
    @SerializedName("data")
    @Expose
    private List<LogHistoryTest> data = null;

    public List<LogHistoryTest> getData() {
        return data;
    }

    public void setData(List<LogHistoryTest> data) {
        this.data = data;
    }
}
