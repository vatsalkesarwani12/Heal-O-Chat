package com.vatsal.kesarwani.therapy.Model;

public class MessageModel {
    private String user;
    private String mssg;

    public MessageModel() {
    }

    public MessageModel(String mssg, String user) {
        this.user = user;
        this.mssg = mssg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMssg() {
        return mssg;
    }

    public void setMssg(String mssg) {
        this.mssg = mssg;
    }
}
