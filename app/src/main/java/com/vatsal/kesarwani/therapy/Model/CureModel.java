package com.vatsal.kesarwani.therapy.Model;

public class CureModel {
    private String name;
    private String desc;
    private String sex;

    public CureModel(String name, String desc, String sex) {
        this.name = name;
        this.desc = desc;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
