package com.kit10.csci448.catastrophe;

import android.support.v4.app.Fragment;

/**
 * Created by Adrien on 3/1/2017.
 */

public class WelcomeActivity extends SingleFragmentActivity {
    public static final String LOG_TAG = "Catastrophe";

    public static final int REQUEST_CODE_GAME = 0;
    public static final int REQUEST_CODE_OPTIONS = 1;

    @Override
    protected Fragment createFragment() {
        return WelcomeFragment.newInstance();
    }
}
