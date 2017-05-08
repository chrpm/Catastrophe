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
    public static final String MUSIC_ON_ID =
            "com.kit10.csci448.catastrophe.music_on_id";
    public static final String SOUND_ON_ID =
            "com.kit10.csci448.catastrophe.sound_on_id";

    public static final int REQUEST_CODE_GAME = 0;
    public static final int REQUEST_CODE_OPTIONS = 1;

    public static Intent newIntent(Context packageContext) {
        Log.d(WelcomeActivity.LOG_TAG, "WelcomeActivity: new intent");
        Intent intent = new Intent(packageContext, WelcomeActivity.class);
        intent.putExtra(MUSIC_ON_ID, true);
        intent.putExtra(SOUND_ON_ID, true);
        return intent;
    }

    public static Intent newIntent(Context packageContext, boolean sound, boolean music) {
        Log.d(WelcomeActivity.LOG_TAG, "WelcomeActivity: new intent");
        Intent intent = new Intent(packageContext, WelcomeActivity.class);
        intent.putExtra(MUSIC_ON_ID, music);
        intent.putExtra(SOUND_ON_ID, sound);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        boolean sound = (boolean) getIntent().getBooleanExtra(SOUND_ON_ID, true);
        boolean music = (boolean) getIntent().getBooleanExtra(MUSIC_ON_ID, true);
        return WelcomeFragment.newInstance(sound, music);
    }
}
