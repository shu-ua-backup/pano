package com.shu.Pano.helpers;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CombinePhoto {

    public static String combine(String nameDir,int count) {
        File path = new File(String.format("/sdcard/Pano/%s/", nameDir)); // base path of the images

// load source images
        Bitmap image = BitmapFactory.decodeFile(String.format("/sdcard/Pano/%s/%d.jpg", nameDir, count));


// create the new image, canvas size is the max. of both image sizes
        int h = image.getHeight();
        int w = image.getWidth() * (count+1);
        Bitmap newImg = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);


        Canvas comboImage = new Canvas(newImg);

        for (int i=count;i>=0;i--) {
            Bitmap img = BitmapFactory.decodeFile(String.format("/sdcard/Pano/%s/%d.jpg", nameDir, i));
            comboImage.drawBitmap(img,img.getWidth()*(count - i),0,null);
        }

        try {

            FileOutputStream out = new FileOutputStream(String.format("/sdcard/Pano/%s/%s.jpg", nameDir, "RESULT"));
            newImg.compress(Bitmap.CompressFormat.JPEG, 70, out);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return String.format("/sdcard/Pano/%s/%s.jpg", nameDir, "RESULT");
    }
}
