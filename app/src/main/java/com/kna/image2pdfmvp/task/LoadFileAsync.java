package com.kna.image2pdfmvp.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.kna.image2pdfmvp.Const;
import com.kna.image2pdfmvp.Utils.FileUtils;
import com.kna.image2pdfmvp.Utils.PDFUtils;
import com.kna.image2pdfmvp.entity.FileModelType;
import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LoadFileAsync extends AsyncTask<Void, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final OnActionCallback callback;
    private final List<FileModelType> list = new ArrayList<>();

    public LoadFileAsync(Context context, OnActionCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        loadPdfCache();
        //     loadConvertedPdfFile();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.callback(Const.KEY_LOADING, (Object) null);
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.callback(Const.KEY_DATA, list);
    }

//    private void loadConvertedPdfFile() {
//        List<FileModelType> temps = new ArrayList<>();
//        String filePath = Environment.getExternalStorageDirectory().toString() + Const.DEFAULT_STORAGE_PDF_FOLDER;
//        File[] files = new File(filePath).listFiles();
//
//        if (files == null) {
//            return;
//        }
//        for (File file : files) {
//            float fileSize = (float) (file.length() / (1024.0 * 1024.0));
//
//            double size = Math.ceil(fileSize * 100) / 100;
//            String date = null;
//            date = getDate(file);
//
//            //int countPage = efficientPDFPageCount(file);
//
//            PdfReader reader = null;
//            int pages = 0;
//            try {
//                RandomAccessFile raf = new RandomAccessFile(file, "r");
//                RandomAccessFileOrArray pdfFile = new RandomAccessFileOrArray(
//                        new RandomAccessSourceFactory().createSource(raf));
//                reader = new PdfReader(pdfFile, new byte[0]);
//                pages = reader.getNumberOfPages();
//                reader.close();
//                temps.add(new FileModelType(file.getAbsolutePath(), FileModelType.TYPE_PDF, pages, size, date, false));
//            } catch (IOException e) {
//                e.printStackTrace();
//                temps.add(new FileModelType(file.getAbsolutePath(), FileModelType.TYPE_PDF_PASSWORD, 0, size, date, false));
//            }
//        }
//        Collections.reverse(temps);
//        list.addAll(temps);
//    }

    private Bitmap pdfToBitmap(File pdfFile) {
        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            final int pageCount = renderer.getPageCount();
            if (pageCount > 0) {
                PdfRenderer.Page page = renderer.openPage(0);
                int width = (int) (page.getWidth());
                int height = (int) (page.getHeight());
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    int efficientPDFPageCount(File file) {

        PdfReader reader = null;
        int pages = 0;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            RandomAccessFileOrArray pdfFile = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(raf));
            reader = new PdfReader(pdfFile, new byte[0]);
            pages = reader.getNumberOfPages();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pages;
    }

    private void loadPdfCache() {
        List<FileModelType> temps = new ArrayList<>();
        File storeDir = FileUtils.getImageStoreDir(context);
        File[] lsDir = storeDir.listFiles();

        if (lsDir == null) {
            return;
        }

        for (File f : lsDir) {
            //  mapToFileModel(temps, f);
        }
        Collections.reverse(temps);
        list.addAll(temps);
    }

//    private void mapToFileModel(List<FileModelType> temps, File f) {
//        double fileSize = 0;
//        int pageSize = 0;
//        String date = null;
//
//        try {
//            pageSize = getPageSize(f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (pageSize == 0) return;
//
//        try {
//            date = getDate(f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            fileSize = getFileSize(f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        FileModelType storeFileModel = new FileModelType(f.getAbsolutePath(), FileModelType.TYPE_IMAGE, pageSize, imageToByte(f.listFiles()[0]), fileSize, date, false);
//        temps.add(storeFileModel);
//    }

    private byte[] imageToByte(File file) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getByteThumb(File f) {
        try {
            Bitmap bitmap = PDFUtils.getPDFThumb(f);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private double getFileSize(File f) {
        long count = 0;
        File[] files = f.listFiles();
        if (files == null) {
            return 0;
        }
        for (int i = 0; i < files.length; i++) {
            count += files[0].length();
        }
        return count;
    }

    private String getDate(File f) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Const.PATTERN_FORMAT_TIME, Locale.getDefault());
        String dateString = dateFormat.format(new File(f.getPath()).lastModified());

        return dateString;
    }

    private int getPageSize(File f) {
        File[] files = f.listFiles();
        if (files != null) {
            return files.length;
        }
        return 0;
    }

    private int checkType(File f) {
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    return FileModelType.TYPE_PDF;
                }
            }
        }
        return FileModelType.TYPE_IMAGE;
    }


}
