package com.jskgmail.indiaskills.pojo;

public class Instruction {
    String language_name;
    String language_code;
    String Instruction;

    public Instruction(String language_name, String language_code, String instruction) {
        this.language_name = language_name;
        this.language_code = language_code;
        Instruction = instruction;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public String getInstruction() {
        return Instruction;
    }

    public void setInstruction(String instruction) {
        Instruction = instruction;
    }
}
