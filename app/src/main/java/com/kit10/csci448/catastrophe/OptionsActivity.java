package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Adrien on 3/1/2017.
 */

public class OptionsActivity extends SingleFragmentActivity {


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
