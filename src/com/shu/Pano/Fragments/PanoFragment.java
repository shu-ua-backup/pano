package com.shu.Pano.Fragments;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.shu.Pano.Objects.Coordinates;
import com.shu.Pano.R;
import com.shu.Pano.helpers.CombinePhoto;
import com.shu.Pano.helpers.CombinePhotoCv;
import com.shu.Pano.helpers.SaveInBackground;

import java.io.IOException;


public class PanoFragment extends Fragment implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private int photoCount;
    private int count;
    private String dirName;
    private long dirNamelong;
    private SurfaceView preview;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private SensorManager mSensorManager;
    private SensorManager mSensorManager1;
    private TextView angleVtw;
    private TextView angleHtw;
    private TextView statusTw;
    private ImageView photoBtn;
    private Sensor mLight;
    private Sensor mLight1;
    private Coordinates next_coord;
    private boolean isFirst;
    private boolean isMinused;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pano_frag,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        camera = Camera.open();
        changeCameraParam(camera.getParameters());

        angleHtw = (TextView) getView().findViewById(R.id.angle_h);
        angleVtw = (TextView) getView().findViewById(R.id.angle_v);
        statusTw = (TextView) getView().findViewById(R.id.status);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mLight =  mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mSensorManager1 = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mLight1 =  mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        int prg = getActivity().getIntent().getIntExtra("photocount",1);
        photoCount = getPhotoCount(prg);
        count = photoCount;

        updateInfo();
        preview = (SurfaceView) getActivity().findViewById(R.id.prewiew);

        surfaceHolder = preview.getHolder();

        surfaceHolder.addCallback(this);

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        photoBtn = (ImageView) getView().findViewById(R.id.photo_btn);


        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirst=true;
                dirNamelong = System.currentTimeMillis();
                dirName = Long.toString(dirNamelong);
                getPhoto();
                photoBtn.setImageResource(R.drawable.camera_icon_red);

            }
        });


        Button btn = (Button) getView().findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = new CombinePhoto().combine(dirName,count-1);
                CombinePhotoCv combinePhotoCv = new CombinePhotoCv();
                combinePhotoCv.combineCv(dirNamelong,count-1);
            }
        });
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
        super.onDestroy();
        if (camera != null)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private void changeCameraParam(Camera.Parameters parameters) {
        parameters.setPreviewFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setExposureCompensation(0);
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);

        camera.setParameters(parameters);

    }

    private void updateInfo() {


        if (photoCount<0) {
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
        }, mLight, SensorManager.SENSOR_DELAY_FASTEST);
    }

    //callback for taking and save photo
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            new SaveInBackground(dirName,photoCount).execute(bytes);
            camera.startPreview();
        }
    };

    private void takePic(final Camera.PictureCallback pc) {
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b) {
                    updateInfo();
                    if(!isMinused) {
                        photoCount--;
                        isMinused=true;
                        camera.takePicture(null, null, null,pc);
                        addAngle();
                    }

                }
            }
        });
    }


    private void getPhoto() {
        //Sensor Event
        SensorEventListener sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (photoCount > 0) {
                    if (isFirst) {
                        isFirst=false;
                        next_coord = new Coordinates(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
                        Log.e("x-coord",Float.toString(photoCount));
                        isMinused=false;
                        takePic(pictureCallback);
                    } else
                    if (Math.round(sensorEvent.values[0]) == Math.round(next_coord.getxAxis())) {
                        Log.e("x-coord",Float.toString(photoCount));
                        isMinused=false;
                        takePic(pictureCallback);
                    } else {
                        navigateHelp((sensorEvent.values[0]));
                    }
                }
                else {
                    statusTw.setText("DONE");
                    photoBtn.setImageResource(R.drawable.camera_icon);
                    mSensorManager1.unregisterListener(this);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

        };

        //HEAD:
        if (photoCount > 0) {

        mSensorManager1.registerListener(sensorEventListener,mLight1,SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            mSensorManager1.unregisterListener(sensorEventListener);
        }
    }


    private int getPhotoCount(int progress) {
        float horAngle = 30;//camera.getParameters().getHorizontalViewAngle();
        if (horAngle > 60) {
            horAngle = 50;
        }
        //horAngle = 40;
        float pr = progress+60;
        int res =  (int)(pr/horAngle)+1;

        return res;
    }

    private void addAngle() {
        float x = next_coord.getxAxis() + 30; //+ camera.getParameters().getHorizontalViewAngle();
        if (x < 360) {
            next_coord.setxAxis(x);
        }
        else {
            next_coord.setxAxis(x-360);
        }

        //statusTw.setText(Float.toString(next_coord.getxAxis()));
    }

    private void navigateHelp(float x) {
        if (x > 50 && next_coord.getxAxis() > x) {
            if (Math.abs(next_coord.getxAxis() - x) < 10) {
                statusTw.setText("near ->");
            } else {
                statusTw.setText("--->");
            }
        }
        else {
            if (Math.abs(next_coord.getxAxis() - x) < 10) {
                statusTw.setText("<- near");
            } else {
                statusTw.setText("<--");
            }
        }
    }





}
