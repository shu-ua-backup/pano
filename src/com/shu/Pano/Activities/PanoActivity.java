package com.shu.Pano.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.Window;
import android.view.WindowManager;
import com.shu.Pano.Fragments.PanoFragment;
import com.shu.Pano.Fragments.StartFragment;
import com.shu.Pano.R;

public class PanoActivity extends FragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pano_act);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.pano_frag, new PanoFragment())
                    .commit();
        }
    }
}
