package com.kna.image2pdfmvp.mvp.model.photoonl;

import com.kna.image2pdfmvp.App;
import com.kna.image2pdfmvp.entity.Media;
import com.kna.image2pdfmvp.mvp.task.ImageSearchTask;

import java.util.List;

public class PhotoOnlRepository {
    private final Response response;

    public PhotoOnlRepository(Response response) {
        this.response = response;
    }

    public void loadImageOnl(String query) {
        new ImageSearchTask(App.getInstance(), (key, data) -> response.onMediaListResult((List<Media>) data[0])).execute(query);
    }
}
