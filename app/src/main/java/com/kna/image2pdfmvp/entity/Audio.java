package com.kna.image2pdfmvp.entity;

public class Audio extends Media {
    private long duration;
    private String artist;

    public Audio(String path, long duration) {
        super(path);
        this.duration = duration;
    }

    public Audio(String path) {
        super(path);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public void setPath(String path) {
        super.setPath(path);
    }
}
