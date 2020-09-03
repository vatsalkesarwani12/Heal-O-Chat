package com.vatsal.kesarwani.therapy.Model;

public class TrackModel {
    private String time;
    private String trackName;

    public TrackModel() {
    }

    public TrackModel(String time, String trackName) {
        this.time = time;
        this.trackName = trackName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    @Override
    public String toString() {
        return "TrackModel{" +
                "time='" + time + '\'' +
                ", trackName='" + trackName + '\'' +
                '}';
    }
}
