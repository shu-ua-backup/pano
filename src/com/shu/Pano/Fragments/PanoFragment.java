package com.shu.Pano.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

import com.shu.Pano.Objects.Coordinates;
import com.shu.Pano.R;
import com.shu.Pano.helpers.SaveInBackground;

import java.io.IOException;


public class PanoFragment extends Fragment implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private int photoCount;
    private SurfaceView preview;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private SensorManager mSensorManager;
    private TextView angleVtw;
    private TextView angleHtw;
    private Sensor mLight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pano_frag,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        angleHtw = (TextView) getView().findViewById(R.id.angle_h);
        angleVtw = (TextView) getView().findViewById(R.id.angle_v);

        photoCount= getActivity().getIntent().getIntExtra("photocount",1);


        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mLight =  mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        updateInfo();
        camera = Camera.open();
        changeCameraParam(camera.getParameters());
        preview = (SurfaceView) getActivity().findViewById(R.id.prewiew);

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
        angleAccel();
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


        if (photoCount<=0) {
            //show result:
        } else {
            TextView tw1 = (TextView) getView().findViewById(R.id.count);
            tw1.setText("Залишилось: " + Integer.toString(photoCount));
        }

    }

    private void angleAccel() {

        mSensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                angleVtw.setText("Horisontal - " + Integer.toString(Math.round(sensorEvent.values[0])));
                angleHtw.setText("x - " + Integer.toString(Math.round(sensorEvent.values[1])));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        }, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private Coordinates getPhoto(final Coordinates prev_coord) {
        final Coordinates next_coord = prev_coord;

        //Phototake callback
        final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                new SaveInBackground().execute(bytes);
                camera.startPreview();
            }
        };

        mSensorManager.registerListener(new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (prev_coord==null) {
                    next_coord.setxAxis(sensorEvent.values[0]);
                    next_coord.setyAxis(sensorEvent.values[1]);
                    next_coord.setzAxis(sensorEvent.values[2]);
                } else {
                    //make photo
                    if (Math.round(sensorEvent.values[0]) == Math.round(prev_coord.getxAxis())) {
                        takePic(pictureCallback);
                        float x = sensorEvent.values[0] + prev_coord.getxAxis();
                            if (x > 360) {
                                next_coord.setxAxis(x);
                            }
                            else {
                                next_coord.setxAxis(x-360);
                            }
                        next_coord.setxAxis(sensorEvent.values[0]+prev_coord.getxAxis());
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

        },mLight,SensorManager.SENSOR_DELAY_GAME);

        return next_coord;
    }

    private void takePic(final Camera.PictureCallback pc) {
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b) {
                    updateInfo();
                    photoCount--;

                    camera.takePicture(null, null, null,pc);
                }
            }
        });
    }

}
