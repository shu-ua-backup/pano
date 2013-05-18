package com.shu.Pano.Fragments;

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

import com.shu.Pano.R;

import java.io.IOException;


public class PanoFragment extends Fragment implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private int photoCount;
    private SurfaceView preview;
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pano_frag,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        photoCount= getActivity().getIntent().getIntExtra("photocount",1);

        updateInfo();
        camera = Camera.open();
        changeCameraParam(camera.getParameters());
        preview = (SurfaceView) getActivity().findViewById(R.id.prewiew);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {
                        photoCount--;
                        updateInfo();
                    }
                });
            }
        });
        surfaceHolder = preview.getHolder();

        surfaceHolder.addCallback(this);

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int i, int i2, int i3) {
        try
        {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Camera.Parameters parameters = camera.getParameters();

        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();

        ViewGroup.LayoutParams lp = preview.getLayoutParams();

        // здесь корректируем размер отображаемого preview, чтобы не было искажений

            lp.width = previewSurfaceWidth;
            lp.height = previewSurfaceHeight;

        preview.setLayoutParams(lp);
        parameters.setPreviewSize(lp.width,lp.height);
        camera.setParameters(parameters);

        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    @Override
    public void onDestroy() {
        camera.release();
        super.onDestroy();
    }

    private void changeCameraParam(Camera.Parameters parameters) {
        parameters.setPreviewFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setExposureCompensation(0);
        parameters.setRotation(90);
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);

        camera.setParameters(parameters);

    }

    private void updateInfo() {
        TextView tw1 = (TextView) getView().findViewById(R.id.count);
        tw1.setText("Залишилось: " + Integer.toString(photoCount));


    }
}
