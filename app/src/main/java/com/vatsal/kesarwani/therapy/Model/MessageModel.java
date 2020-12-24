package com.vatsal.kesarwani.therapy.Model;

public class MessageModel {
    private String user;
    private String mssg;
    private String img;
    private String time;
    private String date;
    private String nodeKey;
    private String sender;
    private String receiver;
    private boolean isseen;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public MessageModel(String user, String mssg, String img, String time, String date, String nodeKey, String sender, String receiver, boolean isseen) {
        this.user = user;
        this.mssg = mssg;
        this.img = img;
        this.time = time;
        this.date = date;
        this.nodeKey = nodeKey;
        this.sender = sender;
        this.receiver = receiver;
        this.isseen = isseen;
    }



    public MessageModel() {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "user='" + user + '\'' +
                ", mssg='" + mssg + '\'' +
                ", img='" + img + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
