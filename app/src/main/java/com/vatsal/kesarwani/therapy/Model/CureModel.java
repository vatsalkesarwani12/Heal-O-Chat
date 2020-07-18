package com.vatsal.kesarwani.therapy.Model;

public class CureModel {
    private String name;
    private String desc;
    private String sex;
    private String mail;

    public CureModel(String name, String desc, String sex,String mail) {
        this.name = name;
        this.desc = desc;
        this.sex = sex;
        this.mail = mail;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
