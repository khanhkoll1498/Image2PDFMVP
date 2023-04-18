package com.kna.image2pdfmvp.mvp.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.kna.image2pdfmvp.entity.FolderImage;
import com.kna.image2pdfmvp.entity.Media;
import com.kna.image2pdfmvp.entity.Photo;
import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class LoadFolderImage extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final OnActionCallback callback;
    private ArrayList<FolderImage> listFolder;

    public LoadFolderImage(Context context, OnActionCallback callback) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listFolder = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Uri uri;
            Cursor cursor;
            int columnData, columnFolder;

            String path;
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.VideoColumns.DURATION};

            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            cursor = context.getContentResolver().query(uri, null, null, null, orderBy + " DESC");

            int colId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            columnData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            columnFolder = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            while (cursor.moveToNext()) {
                int mediaId = cursor.getInt(colId);
                path = cursor.getString(columnData);
                if (!new File(path).exists()) {
                    continue;
                }

                int position = findFolderIndex(cursor, columnFolder);

                ArrayList<Media> mediaList = new ArrayList<>();
                Photo media = new Photo(path);
                media.setMediaId(mediaId);
                mediaList.add(media);

                if (position >= 0) {
                    mediaList.addAll(listFolder.get(position).getMediaList());
                    listFolder.get(position).setMediaList(mediaList);
                } else {
                    FolderImage objModel = new FolderImage();
                    objModel.setFolderName(cursor.getString(columnFolder));
                    objModel.setMediaList(mediaList);
                    listFolder.add(objModel);
                }

            }
            FolderImage folderAllImage = new FolderImage();
            folderAllImage.setFolderName("All images");
            folderAllImage.setMediaList(getMedialList(listFolder));
            listFolder.add(0, folderAllImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Media> getMedialList(ArrayList<FolderImage> listFolder) {
        List<Media> list = new ArrayList<>();
        for (int i = 0; i < listFolder.size(); i++) {
            list.addAll(listFolder.get(i).getMediaList());
        }
        return list;
    }

    private int findFolderIndex(Cursor cursor, int columnFolder) {
        for (int i = 0; i < listFolder.size(); i++) {
            if (listFolder.get(i).getFolderName() == null || cursor.getString(columnFolder) == null)
                continue;
            if (listFolder.get(i).getFolderName().equals(cursor.getString(columnFolder))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (callback != null) {
            callback.callback(null, listFolder);
        }
    }

}
