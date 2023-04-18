package com.kna.image2pdfmvp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import java.io.File;
import java.io.FileOutputStream;

public class DecryptTask extends AsyncTask<String, Void, String> {
    private OnActionCallback callback;

    public void setCallback(OnActionCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... values) {
        String filePath = values[0];
        String pass = values[1];
        return doDecryptPdf(filePath, pass);
    }

    public String doDecryptPdf(String filePath, String pass) {
        File file = new File(filePath);
        File temp = new File(file.getParent(), "test_decrypt.pdf");
        try {
            PdfReader reader = new PdfReader(filePath, pass.getBytes());
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(temp.getAbsolutePath()));
            stamper.setEncryption("".getBytes(), "".getBytes(), EncryptTask.AutoShapeType.DIAGONAL_STRIPE, PdfWriter.STANDARD_ENCRYPTION_128);
            stamper.close();
            reader.close();

            temp.renameTo(file);
            temp.delete();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "Remove password success";
    }

    @Override
    protected void onPostExecute(String value) {
        super.onPostExecute(value);
//        callback.callback("", value);
        Log.d("android_log", "onPostExecute: OKE");
    }

}
