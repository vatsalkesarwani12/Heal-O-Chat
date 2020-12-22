package com.vatsal.kesarwani.therapy.Model;

public class ChatModel {
    private String mail;
    private long time;

    public ChatModel(String mail, long time) {
        this.mail = mail;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
