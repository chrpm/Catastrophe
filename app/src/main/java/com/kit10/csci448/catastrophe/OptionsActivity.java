package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Adrien on 3/1/2017.
 */

public class OptionsActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }


    public static Intent newIntent(Context packageContext) {
        Log.d(WelcomeActivity.LOG_TAG, "OptionsActivity: new intent");
        Intent intent = new Intent(packageContext, OptionsActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        return OptionsFragment.createFragment();
    }
}
