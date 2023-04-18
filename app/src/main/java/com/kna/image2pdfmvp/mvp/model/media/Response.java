package com.kna.image2pdfmvp.mvp.model.media;

import com.kna.image2pdfmvp.entity.FolderImage;
import com.kna.image2pdfmvp.entity.Media;

import java.util.List;

public interface Response {
    void onMediaListResult(List<Media> list);

    void onFolderImageListResult(List<FolderImage> list);
}
