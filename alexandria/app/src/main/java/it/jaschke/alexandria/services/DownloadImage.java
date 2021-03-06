package it.jaschke.alexandria.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import it.jaschke.alexandria.Utilities;

/**
 * Created by saj on 11/01/15.
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Context mContext;

    public DownloadImage(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.mContext = context;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap bookCover = null;
        if(Utilities.checkNetworkAvailable(mContext)) {
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                bookCover = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
        return bookCover;

    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

