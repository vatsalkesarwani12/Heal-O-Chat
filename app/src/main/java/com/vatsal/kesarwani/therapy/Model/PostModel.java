package com.vatsal.kesarwani.therapy.Model;

public class PostModel {
    private String uri;
    private int likes;
    private String message;
    private String by;
    private String id;
    private boolean clicked;

    public PostModel(String uri, int likes, String message, String by,String id,boolean clicked) {
        this.uri = uri;
        this.likes = likes;
        this.message = message;
        this.by = by;
        this.id=id;
        this.clicked=clicked;
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
}
