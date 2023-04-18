package com.kna.image2pdfmvp.mvp.view.screen.media;

import com.kna.image2pdfmvp.base.BaseView;
import com.kna.image2pdfmvp.entity.FolderImage;
import com.kna.image2pdfmvp.entity.Media;

import java.util.List;

public interface MediaView extends BaseView {
    void onMediaListResult(List<Media> list);

    void onFolderImageListResult(List<FolderImage> list);
}
