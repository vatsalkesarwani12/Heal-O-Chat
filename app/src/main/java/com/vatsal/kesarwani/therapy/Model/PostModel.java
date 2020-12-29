package com.vatsal.kesarwani.therapy.Model;

public class PostModel {
    private String uri;
    private int likes;
    private String message;
    private String by;
    private String id;
    private boolean clicked;
    private String name;
    private String profile_display;
    private String uid;
    private int report;
    private long time;

    public PostModel(String uri, int likes, String message, String by,String id,boolean clicked, String name, String profile_display, String uid, int report, long time) {
        this.uri = uri;
        this.likes = likes;
        this.message = message;
        this.by = by;
        this.id=id;
        this.clicked=clicked;
        this.name= name;
        this.profile_display= profile_display;
        this.uid= uid;
        this.report= report;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_display() {
        return profile_display;
    }

    public void setProfile_display(String profile_display) {
        this.profile_display = profile_display;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }
}
