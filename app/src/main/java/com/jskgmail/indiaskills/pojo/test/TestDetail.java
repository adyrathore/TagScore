package com.jskgmail.indiaskills.pojo.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestDetail {
    @SerializedName("schedule_id_pk")
    @Expose
    private String scheduleIdPk;
    @SerializedName("schedule_name")
    @Expose
    private String scheduleName;
    @SerializedName("batch_id_fk")
    @Expose
    private String batchIdFk;
    @SerializedName("range_marking")
    @Expose
    private String rangeMarking;
    @SerializedName("Type")
    @Expose
    private Object type;
    @SerializedName("permission")
    @Expose
    private String permission;
    @SerializedName("is_survey")
    @Expose
    private Object isSurvey;
    @SerializedName("second_random")
    @Expose
    private String secondRandom;
    @SerializedName("schedule_setting")
    @Expose
    private String scheduleSetting;
    @SerializedName("training_center")
    @Expose
    private String trainingCenter;
    @SerializedName("is_offline")
    @Expose
    private String isOffline;
    @SerializedName("question_paper_id")
    @Expose
    private String questionPaperId;
    @SerializedName("test_duration")
    @Expose
    private String testDuration;
    @SerializedName("test_rendomClick")
    @Expose
    private String testRendomClick;
    @SerializedName("test_name")
    @Expose
    private String testName;
    @SerializedName("test_details")
    @Expose
    private String testDetails;
    @SerializedName("target_candidates")
    @Expose
    private String targetCandidates;
    @SerializedName("test_descriptions")
    @Expose
    private String testDescriptions;

    public String getScheduleIdPk() {
        return scheduleIdPk;
    }

    public void setScheduleIdPk(String scheduleIdPk) {
        this.scheduleIdPk = scheduleIdPk;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getBatchIdFk() {
        return batchIdFk;
    }

    public void setBatchIdFk(String batchIdFk) {
        this.batchIdFk = batchIdFk;
    }

    public String getRangeMarking() {
        return rangeMarking;
    }

    public void setRangeMarking(String rangeMarking) {
        this.rangeMarking = rangeMarking;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Object getIsSurvey() {
        return isSurvey;
    }

    public void setIsSurvey(Object isSurvey) {
        this.isSurvey = isSurvey;
    }

    public String getSecondRandom() {
        return secondRandom;
    }

    public void setSecondRandom(String secondRandom) {
        this.secondRandom = secondRandom;
    }

    public String getScheduleSetting() {
        return scheduleSetting;
    }

    public void setScheduleSetting(String scheduleSetting) {
        this.scheduleSetting = scheduleSetting;
    }

    public String getTrainingCenter() {
        return trainingCenter;
    }

    public void setTrainingCenter(String trainingCenter) {
        this.trainingCenter = trainingCenter;
    }

    public String getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(String isOffline) {
        this.isOffline = isOffline;
    }

    public String getQuestionPaperId() {
        return questionPaperId;
    }

    public void setQuestionPaperId(String questionPaperId) {
        this.questionPaperId = questionPaperId;
    }

    public String getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(String testDuration) {
        this.testDuration = testDuration;
    }

    public String getTestRendomClick() {
        return testRendomClick;
    }

    public void setTestRendomClick(String testRendomClick) {
        this.testRendomClick = testRendomClick;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(String testDetails) {
        this.testDetails = testDetails;
    }

    public String getTargetCandidates() {
        return targetCandidates;
    }

    public void setTargetCandidates(String targetCandidates) {
        this.targetCandidates = targetCandidates;
    }

    public String getTestDescriptions() {
        return testDescriptions;
    }

    public void setTestDescriptions(String testDescriptions) {
        this.testDescriptions = testDescriptions;
    }

}
