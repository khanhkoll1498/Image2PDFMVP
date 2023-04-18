package com.kna.image2pdfmvp.mvp.model.media;

import com.kna.image2pdfmvp.App;
import com.kna.image2pdfmvp.entity.FolderImage;
import com.kna.image2pdfmvp.entity.Media;
import com.kna.image2pdfmvp.mvp.task.LoadAudioFile;
import com.kna.image2pdfmvp.mvp.task.LoadFolderImage;
import com.kna.image2pdfmvp.mvp.task.LoadVideoFile;

import java.util.ArrayList;
import java.util.List;

public class MediaRepository {
    private final Response response;

    public MediaRepository(Response response) {
        this.response = response;
    }

    public void loadFolderImageList() {
        new LoadFolderImage(App.getInstance(), (key, data) -> {
            response.onFolderImageListResult((List<FolderImage>) data[0]);
        }).execute();
    }

    public void loadAudioList() {
        new LoadAudioFile(App.getInstance(), (key, data) -> {
            response.onMediaListResult((List<Media>) data[0]);
        }).execute();
    }

    public void loadVideoList() {
        new LoadVideoFile(App.getInstance(), (key, data) -> {
            response.onMediaListResult((List<Media>) data[0]);
        }).execute();
    }

    public void searchMedia(String text, List<Media> list) {
        List<Media> mediaList = new ArrayList<>();
        for (Media media : list) {
            if (media.getPath().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                mediaList.add(media);
            }
        }
        response.onMediaListResult(mediaList);
    }
}
