package com.jskgmail.indiaskills.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserTopicViceReport {

    @SerializedName("topic_id_fk")
    @Expose
    private String topicIdFk;
    @SerializedName("topic_name")
    @Expose
    private String topicName;
    @SerializedName("gain_marks")
    @Expose
    private String gainMarks;
    @SerializedName("question_marks")
    @Expose
    private String questionMarks;

    public String getTopicIdFk() {
        return topicIdFk;
    }

    public void setTopicIdFk(String topicIdFk) {
        this.topicIdFk = topicIdFk;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getGainMarks() {
        return gainMarks;
    }

    public void setGainMarks(String gainMarks) {
        this.gainMarks = gainMarks;
    }

    public String getQuestionMarks() {
        return questionMarks;
    }

    public void setQuestionMarks(String questionMarks) {
        this.questionMarks = questionMarks;
    }

}
