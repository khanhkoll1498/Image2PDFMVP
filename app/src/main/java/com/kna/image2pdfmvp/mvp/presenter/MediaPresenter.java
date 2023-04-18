package com.kna.image2pdfmvp.mvp.presenter;

import com.kna.image2pdfmvp.base.BasePresenter;
import com.kna.image2pdfmvp.entity.FolderImage;
import com.kna.image2pdfmvp.entity.Media;
import com.kna.image2pdfmvp.mvp.model.media.MediaRepository;
import com.kna.image2pdfmvp.mvp.model.media.Response;
import com.kna.image2pdfmvp.mvp.view.screen.media.MediaView;

import java.util.List;

public class MediaPresenter extends BasePresenter<MediaView> implements Response {
    private final MediaRepository mediaRepository;

    public MediaPresenter(MediaView mView) {
        super(mView);
        mediaRepository = new MediaRepository(this);
    }


    public void loadFolderImageList() {
        mediaRepository.loadFolderImageList();
    }


    public void loadAudioList() {
        mediaRepository.loadAudioList();
    }

    public void loadVideoList() {
        mediaRepository.loadVideoList();
    }


    @Override
    public void onMediaListResult(List<Media> list) {
        mView.onMediaListResult(list);
    }

    @Override
    public void onFolderImageListResult(List<FolderImage> list) {
        mView.onFolderImageListResult(list);
    }

    public void searchMedia(String text, List<Media> list) {
        mediaRepository.searchMedia(text, list);
    }
}
