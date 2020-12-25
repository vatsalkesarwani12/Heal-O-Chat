package com.vatsal.kesarwani.therapy.Model;

public class ChatModelDetails {
    private String name, uid, sex, dp, mail;
    private boolean online;

    public ChatModelDetails() {
    }

    public ChatModelDetails(String name, String uid, String sex, String dp, String mail, boolean online) {
        this.name = name;
        this.uid = uid;
        this.sex = sex;
        this.dp = dp;
        this.mail = mail;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
