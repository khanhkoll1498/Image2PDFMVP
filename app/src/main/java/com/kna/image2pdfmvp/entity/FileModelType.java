package com.kna.image2pdfmvp.entity;

import android.content.Context;

import java.io.Serializable;

public class FileModelType implements Serializable {
    public static final int TYPE_PDF = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_PDF_PASSWORD = 3;
    private String path;
    private int type;
    private int pageSize;
    private double size;
    private String date;
    private boolean isSelect;
    private Context context;

    public FileModelType(String path, int type, int pageSize, double size, String date, boolean isSelect, Context context) {
        this.path = path;
        this.type = type;
        this.pageSize = pageSize;
        this.size = size;
        this.date = date;
        this.isSelect = isSelect;
        this.context = context;
    }
}
