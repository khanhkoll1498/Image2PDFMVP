package com.kna.image2pdfmvp.entity;

import java.io.Serializable;
import java.util.List;

public class FolderImage implements Serializable {
    private String FolderName;
    private List<Media> list;

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public List<Media> getMediaList() {
        return list;
    }

    public void setMediaList(List<Media> list) {
        this.list = list;
    }
}