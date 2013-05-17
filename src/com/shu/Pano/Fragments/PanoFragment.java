package com.shu.Pano.Fragments;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shu.Pano.R;


public class PanoFragment extends Fragment {

    private int photoCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pano_frag,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tw1 = (TextView) getView().findViewById(R.id.count);
        photoCount= getActivity().getIntent().getIntExtra("photocount",1);
        tw1.setText(Integer.toString(photoCount));

    }
}
