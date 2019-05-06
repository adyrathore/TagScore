package com.jskgmail.indiaskills.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserNosWiseDetail {

    @SerializedName("module_id_fk")
    @Expose
    private String moduleIdFk;
    @SerializedName("module_name")
    @Expose
    private String moduleName;
    @SerializedName("gain_marks")
    @Expose
    private String gainMarks;
    @SerializedName("Total_marks")
    @Expose
    private String totalMarks;

    public String getModuleIdFk() {
        return moduleIdFk;
    }

    public void setModuleIdFk(String moduleIdFk) {
        this.moduleIdFk = moduleIdFk;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getGainMarks() {
        return gainMarks;
    }

    public void setGainMarks(String gainMarks) {
        this.gainMarks = gainMarks;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

}