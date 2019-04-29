package com.jskgmail.indiaskills.pojo.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
