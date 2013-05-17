package com.shu.Pano.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.shu.Pano.Fragments.StartFragment;
import com.shu.Pano.R;

public class StartActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_act);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.start_frag, new StartFragment())
                    .commit();
        }
    }
}
