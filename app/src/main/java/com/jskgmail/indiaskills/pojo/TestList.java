package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by aditya.singh on 12/12/2018.
 */

public class TestList implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uniqueID")
    @Expose
    private String uniqueID;
    @SerializedName("purchasedTime")
    @Expose
    private String purchasedTime;
    @SerializedName("testName")
    @Expose
    private String testName;
    @SerializedName("schedule_id_pk")
    @Expose
    private String scheduleIdPk;
    @SerializedName("batch_id_fk")
    @Expose
    private String batchIdFk;
    @SerializedName("test_type")
    @Expose
    private String testType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getPurchasedTime() {
        return purchasedTime;
    }

    public void setPurchasedTime(String purchasedTime) {
        this.purchasedTime = purchasedTime;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getScheduleIdPk() {
        return scheduleIdPk;
    }

    public void setScheduleIdPk(String scheduleIdPk) {
        this.scheduleIdPk = scheduleIdPk;
    }

    public String getBatchIdFk() {
        return batchIdFk;
    }

    public void setBatchIdFk(String batchIdFk) {
        this.batchIdFk = batchIdFk;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

}
