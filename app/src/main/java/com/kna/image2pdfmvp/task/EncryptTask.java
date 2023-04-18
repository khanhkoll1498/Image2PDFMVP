package com.kna.image2pdfmvp.task;

import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import java.io.File;
import java.io.FileOutputStream;

public class EncryptTask extends AsyncTask<String, Void, Void> {
    private OnActionCallback callback;

    public void setCallback(OnActionCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... values) {
        String filePath = values[0];
        String pass = values[1];
        doEncryption(filePath, pass);
        return null;
    }

    public void doEncryption(String filePath, String pass) {
        File file = new File(filePath);
        File temp = new File(file.getParent(), "test_encrypt.pdf");
        try {
            PdfReader.unethicalreading = true;
            PdfReader pdfReader = new PdfReader(file.getAbsolutePath());
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(temp.getAbsolutePath()));
            pdfStamper.setEncryption(pass.getBytes(), pass.getBytes(), AutoShapeType.DIAGONAL_STRIPE, PdfWriter.STANDARD_ENCRYPTION_128);
            pdfStamper.close();
            pdfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        temp.renameTo(file);
        temp.delete();

    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
//        callback.callback("", "");
    }

    public class AutoShapeType {
        public static final int DIAGONAL_STRIPE = 2068;

        private AutoShapeType() {
        }
    }
}
