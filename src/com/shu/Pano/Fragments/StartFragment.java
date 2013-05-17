package com.shu.Pano.Fragments;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shu.Pano.Activities.PanoActivity;
import com.shu.Pano.R;

public class StartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_frag,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SeekBar stAngleSB = (SeekBar) getView().findViewById(R.id.start_angle);

        Button btn = (Button) getView().findViewById(R.id.camera_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PanoActivity.class);
                intent.putExtra("photocount",getPhotoCount(stAngleSB));
             //   Toast.makeText(getView().getContext(), Integer.toString(getPhotoCount(stAngleSB)), Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
        stAngleSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView tw = (TextView) getView().findViewById(R.id.textView);
                tw.setText(Integer.toString(i+60));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int getPhotoCount(SeekBar seekBar) {
        final Camera camera = Camera.open();
        float horAngle = camera.getParameters().getHorizontalViewAngle();
        if (horAngle > 60) {
            horAngle = 50;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                camera.release();
            }
        });
        thread.start();

        float pr = seekBar.getProgress()+60;
        int res =  (int)(pr/horAngle)+1;

        return res;
    }
}
