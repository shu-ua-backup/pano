package com.shu.Pano.helpers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CombinePhotoCv{

    private Mat panorama;

    public void combineCv(Long nameDir,int cnt,Context context) {



        Bitmap image = BitmapFactory.decodeFile(String.format("/sdcard/Pano/%s/%d.jpg", nameDir, cnt));

        int h = image.getHeight();
        int w = image.getWidth();// * (cnt+1);
        panorama = new Mat(h, w, CvType.CV_8UC3);
        StitchIt(nameDir,cnt,panorama.getNativeObjAddr());
    }

    public native void StitchIt(long nameDir,int count,long pano);

}
