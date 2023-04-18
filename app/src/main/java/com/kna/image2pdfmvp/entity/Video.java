package com.kna.image2pdfmvp.entity;

import java.io.Serializable;

public class Video extends Media implements Serializable {

    private long duration;
    private String resolution;

    public Video(String path) {
        super(path);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
