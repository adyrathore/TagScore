package com.jskgmail.indiaskills.pojo;

public class BatchInfo {
    String topic;
    String info;

    public BatchInfo(String topic, String info) {
        this.topic = topic;
        this.info = info;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
