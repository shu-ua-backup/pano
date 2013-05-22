package com.shu.Pano.helpers;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

public class CombinePhoto {

    private static String nameDir;
    private static int count;


    public CombinePhoto(String nameDir,int count) {
        this.nameDir=nameDir;
        this.count=count;
    }

    public static String combine() {
        File path = new File(String.format("/sdcard/Pano/%s/", nameDir)); // base path of the images

// load source images
        Bitmap image = BitmapFactory.decodeFile(String.format("/sdcard/Pano/%s/%d.jpg", nameDir, 1));
        Bitmap image1 = BitmapFactory.decodeFile(String.format("/sdcard/Pano/%s/%d.jpg", nameDir, 2));


// create the new image, canvas size is the max. of both image sizes
        int w = Math.max(image.getWidth(), image1.getWidth());
        int h = image.getHeight() + image1.getHeight();
        Bitmap newImg = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);

// paint both images, preserving the alpha channels
        for(int x = 0; x < newImg.getWidth(); x++)
        {
            for(int y = 0; y < newImg.getHeight(); y++)
            {
                if (y < newImg.getHeight()) {
                    newImg.setPixel(x,y,image.getPixel(x,y));
                } else {
                    newImg.setPixel(x,y,image1.getPixel(x,y-image1.getWidth()));
                }
            }
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
