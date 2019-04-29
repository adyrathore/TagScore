package com.jskgmail.indiaskills.pojo.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BatchDetail {
    @SerializedName("user_id_fk")
    @Expose
    private String userIdFk;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;

    public String getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(String userIdFk) {
        this.userIdFk = userIdFk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
