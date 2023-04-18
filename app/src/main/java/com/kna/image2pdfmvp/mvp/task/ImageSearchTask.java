package com.kna.image2pdfmvp.mvp.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.kna.image2pdfmvp.entity.Media;
import com.kna.image2pdfmvp.entity.Photo;
import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageSearchTask extends AsyncTask<String, Void, Void> {
    private Context context;
    private OnActionCallback callback;
    private List<Media> mediaList = new ArrayList<>();

    public ImageSearchTask(Context context, OnActionCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... strings) {
//        String url = "https://www.google.com/search?tbm=isch&tbs=isz:lt,islt:2mp&q=" + strings[0].trim().toLowerCase();
        String url = "https://www.google.com/search?as_st=y&tbm=isch&as_q=" + strings[0].trim().toLowerCase() + "&as_epq=&as_oq=&as_eq=&cr=&as_sitesearch=&safe=images&tbs=isz:lt,islt:70mp,ift:jpg";
        try {
            Document document = Jsoup.connect(url).get();
            Elements elementList = document.getElementsByTag("img");
            for (Element e : elementList) {
                Elements img = e.getElementsByTag("img");
                try {
                    String src = img.get(0).attr("data-src");
                    if (TextUtils.isEmpty(src)) {
                        continue;
                    }
                    mediaList.add(new Photo(src));
                    Log.d("android_log", "doInBackground: " + src);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if (callback != null) {
            callback.callback(null, mediaList);
        }
    }
}
