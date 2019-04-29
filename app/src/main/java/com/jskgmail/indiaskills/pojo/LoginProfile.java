package com.jskgmail.indiaskills.pojo;

/**
 * Created by aditya.singh on 12/12/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginProfile implements Serializable{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_profile_picture")
    @Expose
    private Object userProfilePicture;
    @SerializedName("qualification_name")
    @Expose
    private Object qualificationName;
    @SerializedName("country_name")
    @Expose
    private Object countryName;
    @SerializedName("city_name")
    @Expose
    private Object cityName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(Object userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public Object getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(Object qualificationName) {
        this.qualificationName = qualificationName;
    }

    public Object getCountryName() {
        return countryName;
    }

    public void setCountryName(Object countryName) {
        this.countryName = countryName;
    }

    public Object getCityName() {
        return cityName;
    }

    public void setCityName(Object cityName) {
        this.cityName = cityName;
    }

}
