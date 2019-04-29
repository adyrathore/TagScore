package com.jskgmail.indiaskills.pojo.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tag_id")
    @Expose
    private Object tagId;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("partner_id_fk")
    @Expose
    private String partnerIdFk;
    @SerializedName("company_id_fk")
    @Expose
    private Object companyIdFk;
    @SerializedName("industry_id_fk")
    @Expose
    private String industryIdFk;
    @SerializedName("courses_id_fk")
    @Expose
    private String coursesIdFk;
    @SerializedName("module_id_fk")
    @Expose
    private String moduleIdFk;
    @SerializedName("topic_id_fk")
    @Expose
    private String topicIdFk;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("type_of_question")
    @Expose
    private String typeOfQuestion;
    @SerializedName("media_type")
    @Expose
    private Object mediaType;
    @SerializedName("media")
    @Expose
    private Object media;
    @SerializedName("question_mark")
    @Expose
    private String questionMark;
    @SerializedName("question_type")
    @Expose
    private String questionType;
    @SerializedName("language_code")
    @Expose
    private String languageCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("multi_tag")
    @Expose
    private String multiTag;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("otherLanguages")
    @Expose
    private List<OtherLanguage> otherLanguages = null;
    @SerializedName("ImageHash")
    @Expose
    private String imageHash;
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getTagId() {
        return tagId;
    }

    public void setTagId(Object tagId) {
        this.tagId = tagId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPartnerIdFk() {
        return partnerIdFk;
    }

    public void setPartnerIdFk(String partnerIdFk) {
        this.partnerIdFk = partnerIdFk;
    }

    public Object getCompanyIdFk() {
        return companyIdFk;
    }

    public void setCompanyIdFk(Object companyIdFk) {
        this.companyIdFk = companyIdFk;
    }

    public String getIndustryIdFk() {
        return industryIdFk;
    }

    public void setIndustryIdFk(String industryIdFk) {
        this.industryIdFk = industryIdFk;
    }

    public String getCoursesIdFk() {
        return coursesIdFk;
    }

    public void setCoursesIdFk(String coursesIdFk) {
        this.coursesIdFk = coursesIdFk;
    }

    public String getModuleIdFk() {
        return moduleIdFk;
    }

    public void setModuleIdFk(String moduleIdFk) {
        this.moduleIdFk = moduleIdFk;
    }

    public String getTopicIdFk() {
        return topicIdFk;
    }

    public void setTopicIdFk(String topicIdFk) {
        this.topicIdFk = topicIdFk;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public Object getMediaType() {
        return mediaType;
    }

    public void setMediaType(Object mediaType) {
        this.mediaType = mediaType;
    }

    public Object getMedia() {
        return media;
    }

    public void setMedia(Object media) {
        this.media = media;
    }

    public String getQuestionMark() {
        return questionMark;
    }

    public void setQuestionMark(String questionMark) {
        this.questionMark = questionMark;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMultiTag() {
        return multiTag;
    }

    public void setMultiTag(String multiTag) {
        this.multiTag = multiTag;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public List<OtherLanguage> getOtherLanguages() {
        return otherLanguages;
    }

    public void setOtherLanguages(List<OtherLanguage> otherLanguages) {
        this.otherLanguages = otherLanguages;
    }

    public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String imageHash) {
        this.imageHash = imageHash;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
