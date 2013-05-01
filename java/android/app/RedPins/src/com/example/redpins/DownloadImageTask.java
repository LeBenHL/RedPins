package com.example.redpins;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
	
    ImageView image;
    String requestPath;

    public DownloadImageTask(ImageView image, String requestPath) {
        this.image = image;
        this.requestPath = requestPath;
    }

    protected Bitmap doInBackground(Void... params) {
        String url = MainActivity.serverURL + requestPath;
        Bitmap bm = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            bm = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bm;
    }

    protected void onPostExecute(Bitmap bm) {
        image.setImageBitmap(bm);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

}
