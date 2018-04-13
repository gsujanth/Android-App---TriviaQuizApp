package com.example.princ.homework03;
/*
  Author: Sujanth Babu Guntupalli
*/

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImageTask extends AsyncTask<String,Void,Bitmap> {

    IImage iImage;

    public GetImageTask(IImage iImage) {
        this.iImage = iImage;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        if(!isCancelled()) {
            Log.d("demo", "doInBackground: "+"thread is not cancelled");
            HttpURLConnection con = null;
            Bitmap bitmap = null;

            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection) url.openConnection();
                con.connect();
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    bitmap = BitmapFactory.decodeStream(con.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return bitmap;
        }else{
            Log.d("demo", "doInBackground: "+"thread is cancelled");
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap!=null) {
            Log.d("demoBitmap", "onPostExecute: "+"not null");
        }else{
            Log.d("demoBitmap", "onPostExecute: "+"is null");
        }
            iImage.handleBitmap(bitmap);

    }

    public static interface IImage{
        public void handleBitmap(Bitmap bitmap);
    }
}
