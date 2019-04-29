package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aditya.singh on 12/12/2018.
 */

public class LogHistoryTest implements Serializable {

    @SerializedName("question_id")
    @Expose
    private String question_id;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("candidate_id")
    @Expose
    private String candidateId;
    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
}