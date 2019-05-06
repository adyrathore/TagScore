package com.jskgmail.indiaskills.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("candidate_email")
    @Expose
    private String candidateEmail;
    @SerializedName("candidate_name")
    @Expose
    private String candidateName;
    @SerializedName("theory")
    @Expose
    private ArrayList<Theory> theory = null;
    @SerializedName("practical")
    @Expose
    private List<Practical> practical = null;
    @SerializedName("obtain_marks")
    @Expose
    private Integer obtainMarks;
    @SerializedName("total_marks")
    @Expose
    private Integer totalMarks;
    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("percentage")
    @Expose
    private Integer percentage;
    @SerializedName("user_nos_wise_details")
    @Expose
    private ArrayList<UserNosWiseDetail> userNosWiseDetails = null;
    @SerializedName("User_Topic_Vice_Report")
    @Expose
    private ArrayList<UserTopicViceReport> userTopicViceReport = null;

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public ArrayList<Theory> getTheory() {
        return theory;
    }

    public void setTheory(ArrayList<Theory> theory) {
        this.theory = theory;
    }

    public List<Practical> getPractical() {
        return practical;
    }

    public void setPractical(List<Practical> practical) {
        this.practical = practical;
    }

    public Integer getObtainMarks() {
        return obtainMarks;
    }

    public void setObtainMarks(Integer obtainMarks) {
        this.obtainMarks = obtainMarks;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public ArrayList<UserNosWiseDetail> getUserNosWiseDetails() {
        return userNosWiseDetails;
    }

    public void setUserNosWiseDetails(ArrayList<UserNosWiseDetail> userNosWiseDetails) {
        this.userNosWiseDetails = userNosWiseDetails;
    }

    public ArrayList<UserTopicViceReport> getUserTopicViceReport() {
        return userTopicViceReport;
    }

    public void setUserTopicViceReport(ArrayList<UserTopicViceReport> userTopicViceReport) {
        this.userTopicViceReport = userTopicViceReport;
    }

}