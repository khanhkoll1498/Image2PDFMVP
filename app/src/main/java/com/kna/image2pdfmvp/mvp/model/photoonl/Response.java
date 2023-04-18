package com.kna.image2pdfmvp.mvp.model.photoonl;

import com.kna.image2pdfmvp.entity.Media;

import java.util.List;

public interface Response {
    void onMediaListResult(List<Media> list);
}
