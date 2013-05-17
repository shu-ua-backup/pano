package com.shu.Pano.Activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.shu.Pano.R;


public class MyActivity extends Activity implements SensorEventListener {
    private Sensor mLight;

    TextView mForceValueText;
    TextView mXValueText;
    TextView mYValueText;
    TextView mZValueText;
    private SensorManager mSensorManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mForceValueText = (TextView)findViewById(R.id.value_force);
        mXValueText = (TextView)findViewById(R.id.value_x);
        mYValueText = (TextView)findViewById(R.id.value_y);
        mZValueText = (TextView)findViewById(R.id.value_z);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight =  mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        mXValueText.setText(Float.toString(sensorEvent.values[0]));
        Log.e("angle",Float.toString(sensorEvent.values[0]));
        mYValueText.setText(Float.toString(sensorEvent.values[1]));
        mZValueText.setText(Float.toString(sensorEvent.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void takePhoto() {

    }
}