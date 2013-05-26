package com.shu.Pano.helpers;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class CombinePhotoCv{

    private Mat panorama;

    public void combineCv(Long nameDir,int cnt) {



        Bitmap image = BitmapFactory.decodeFile(String.format("/sdcard/Pano/%s/%d.jpg", nameDir, cnt));

        int h = image.getHeight();
        int w = image.getWidth() * (cnt+1);
        panorama = new Mat(h, w, CvType.CV_8UC3);
        StitchIt(nameDir,cnt,panorama.getNativeObjAddr());
        savePano(panorama,nameDir);
    }

    private void savePano(Mat image,Long nameDir)
    {
        Highgui.imwrite(String.format("/sdcard/Pano/%s/RESULTCV.jpg", nameDir), image);
    }

    public native void StitchIt(long nameDir,int count,long pano);

}
