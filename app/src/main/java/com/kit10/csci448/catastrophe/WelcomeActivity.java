package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Adrien on 3/1/2017.
 */

public class WelcomeActivity extends SingleFragmentActivity {
    public static final String LOG_TAG = "Catastrophe";

    public static final int REQUEST_CODE_GAME = 0;
    public static final int REQUEST_CODE_OPTIONS = 1;

    public static Intent newIntent(Context packageContext) {
        Log.d(WelcomeActivity.LOG_TAG, "WelcomeActivity: new intent");
        Intent intent = new Intent(packageContext, WelcomeActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return WelcomeFragment.newInstance();
    }
}
