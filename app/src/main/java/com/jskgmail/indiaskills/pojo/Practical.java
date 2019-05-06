package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Practical {

    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("type_of_question")
    @Expose
    private String typeOfQuestion;
    @SerializedName("answer")
    @Expose
    private Object answer;
    @SerializedName("marks_gain")
    @Expose
    private String marksGain;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("boomarkedCount")
    @Expose
    private String boomarkedCount;
    @SerializedName("TimeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("attemptCount")
    @Expose
    private String attemptCount;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTypeOfQuestion() {
        return typeOfQuestion;
    }

    public void setTypeOfQuestion(String typeOfQuestion) {
        this.typeOfQuestion = typeOfQuestion;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public String getMarksGain() {
        return marksGain;
    }

    public void setMarksGain(String marksGain) {
        this.marksGain = marksGain;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBoomarkedCount() {
        return boomarkedCount;
    }

    public void setBoomarkedCount(String boomarkedCount) {
        this.boomarkedCount = boomarkedCount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(String attemptCount) {
        this.attemptCount = attemptCount;
    }

}
