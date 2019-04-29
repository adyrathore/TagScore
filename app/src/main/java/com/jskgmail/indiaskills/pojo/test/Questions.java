package com.jskgmail.indiaskills.pojo.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Questions {
    @SerializedName("theoryQuestions")
    @Expose
    private List<QuestionData> theoryQuestions = null;
    @SerializedName("practical_questions")
    @Expose
    private List<QuestionData> practicalQuestions = null;

    public List<QuestionData> getTheoryQuestions() {
        return theoryQuestions;
    }

    public void setTheoryQuestions(List<QuestionData> theoryQuestions) {
        this.theoryQuestions = theoryQuestions;
    }

    public List<QuestionData> getPracticalQuestions() {
        return practicalQuestions;
    }

    public void setPracticalQuestions(List<QuestionData> practicalQuestions) {
        this.practicalQuestions = practicalQuestions;
    }

}
