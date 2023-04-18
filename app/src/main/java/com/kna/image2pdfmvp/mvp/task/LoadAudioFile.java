package com.kna.image2pdfmvp.mvp.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.kna.image2pdfmvp.entity.Audio;
import com.kna.image2pdfmvp.entity.Media;
import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

@SuppressLint("StaticFieldLeak")
public class LoadAudioFile extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final OnActionCallback callback;
    private final ArrayList<Media> list = new ArrayList<>();

    public LoadAudioFile(Context context, OnActionCallback callback) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            final String orderBy = MediaStore.Audio.Media.DATE_TAKEN;
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
        int columnArtist = cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST);
        while (cursor.moveToNext()) {
            int mediaId = cursor.getInt(colId);
            String path = cursor.getString(columnData);
            long duration = cursor.getLong(columnDuration);
            String artist = cursor.getString(columnArtist);
            if (!new File(path).exists()) {
                continue;
            }
            Audio media = new Audio(path, duration);
            media.setMediaId(mediaId);
            media.setArtist(artist);
            list.add(media);
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (callback != null) {
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = i; j < list.size(); j++) {
                    if (new File(list.get(i).getPath()).lastModified() < new File(list.get(j).getPath()).lastModified()) {
                        Collections.swap(list, i, j);
                    }
                }
            }
            callback.callback(null, list);
        }
    }

}
