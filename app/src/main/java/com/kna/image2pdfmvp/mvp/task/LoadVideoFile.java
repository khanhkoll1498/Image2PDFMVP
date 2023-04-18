package com.kna.image2pdfmvp.mvp.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.kna.image2pdfmvp.entity.Media;
import com.kna.image2pdfmvp.entity.Video;
import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class LoadVideoFile extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final OnActionCallback callback;
    List<Media> mediaList = new ArrayList<>();

    public LoadVideoFile(Context context, OnActionCallback callback) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            cursor = context.getContentResolver().query(uri, null, null, null, orderBy + " DESC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            return null;
        }
        int colId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
        int columnData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDuration = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION);
        int colSolution = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RESOLUTION);
        while (cursor.moveToNext()) {
            int mediaId = cursor.getInt(colId);
            String path = cursor.getString(columnData);
            long duration = cursor.getLong(columnDuration);
            String resolution = cursor.getString(colSolution);
            File file = new File(path);
            if (!file.exists()) {
                continue;
            }
//            String resolution = "1920 x 720";
//            try {
//                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
//                metaRetriever.setDataSource(path);
//                String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
//                String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
//                resolution = width + "x" + height;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//            int id = cursor.getInt(idColumn);
//            Bitmap bitmap = CacheUtils.getInstance().getBitmap(path);
//            if (bitmap == null) {
//                try {
//                    bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Video.Thumbnails.MINI_KIND, null);
//                } catch (Exception e) {
//                    bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
//                }
//            }
//            CacheUtils.getInstance().putBitmap(path, bitmap);
            Video video = new Video(path);
            video.setMediaId(mediaId);
            video.setDuration(duration);
            video.setResolution(resolution);
            mediaList.add(video);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (callback != null) {
            callback.callback(null, mediaList);
        }
    }

}
