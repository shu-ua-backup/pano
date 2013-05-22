package com.shu.Pano.helpers;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;

public class SaveInBackground extends AsyncTask<byte[], String, String> {

    private int name;
    private String dirname;

    public SaveInBackground (String dirname,int name){
        this.name = name;
        this.dirname = dirname;
    }
    @Override
    protected String doInBackground(byte[]... arrayOfByte) {
        try {
            File saveDir = new File(String.format("/sdcard/Pano/%s/", dirname));

            if (!saveDir.exists())
            {
                saveDir.mkdirs();
            }

            FileOutputStream os =
                    new FileOutputStream(String.format("/sdcard/Pano/%s/%d.jpg", dirname, name));
            os.write(arrayOfByte[0]);
            os.close();
        } catch (Exception e) {
            //
        }
        return(null);
    }
}
