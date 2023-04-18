package com.kna.image2pdfmvp.Utils;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.IOException;

public class PDFUtils {

    public static Bitmap getPDFThumb(File pdfFile) {
        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            int pageCount = renderer.getPageCount();
            if (pageCount > 0) {
                PdfRenderer.Page page = renderer.openPage(0);
                bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    private class AutoShapeType {
        public static final int DIAGONAL_STRIPE = 2068;

        private AutoShapeType() {
        }

    }
}
