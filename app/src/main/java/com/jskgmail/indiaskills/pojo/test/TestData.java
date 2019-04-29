package com.jskgmail.indiaskills.pojo.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestData {
    @SerializedName("test_languages")
    @Expose
    private List<TestLanguage> testLanguages = null;
    @SerializedName("test_details")
    @Expose
    private List<TestDetail> testDetails = null;
    @SerializedName("schedule_setttings")
    @Expose
    private ScheduleSetttings scheduleSetttings;
    @SerializedName("batch_details")
    @Expose
    private List<BatchDetail> batchDetails = null;
    @SerializedName("total_candidate")
    @Expose
    private Integer totalCandidate;
    @SerializedName("Instructions")
    @Expose
    private List<Object> instructions = null;
    @SerializedName("questions")
    @Expose
    private Questions questions;
    @SerializedName("assessor_feedback_quesitons")
    @Expose
    private List<AssessorFeedbackQuesiton> assessorFeedbackQuesitons = null;
    @SerializedName("video_question_count")
    @Expose
    private Integer videoQuestionCount;

    public List<TestLanguage> getTestLanguages() {
        return testLanguages;
    }

    public void setTestLanguages(List<TestLanguage> testLanguages) {
        this.testLanguages = testLanguages;
    }

    public List<TestDetail> getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(List<TestDetail> testDetails) {
        this.testDetails = testDetails;
    }

    public ScheduleSetttings getScheduleSetttings() {
        return scheduleSetttings;
    }

    public void setScheduleSetttings(ScheduleSetttings scheduleSetttings) {
        this.scheduleSetttings = scheduleSetttings;
    }

    public List<BatchDetail> getBatchDetails() {
        return batchDetails;
    }

    public void setBatchDetails(List<BatchDetail> batchDetails) {
        this.batchDetails = batchDetails;
    }

    public Integer getTotalCandidate() {
        return totalCandidate;
    }

    public void setTotalCandidate(Integer totalCandidate) {
        this.totalCandidate = totalCandidate;
    }

    public List<Object> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Object> instructions) {
        this.instructions = instructions;
    }

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
    }

    public List<AssessorFeedbackQuesiton> getAssessorFeedbackQuesitons() {
        return assessorFeedbackQuesitons;
    }

    public void setAssessorFeedbackQuesitons(List<AssessorFeedbackQuesiton> assessorFeedbackQuesitons) {
        this.assessorFeedbackQuesitons = assessorFeedbackQuesitons;
    }

    public Integer getVideoQuestionCount() {
        return videoQuestionCount;
    }

    public void setVideoQuestionCount(Integer videoQuestionCount) {
        this.videoQuestionCount = videoQuestionCount;
    }
}
