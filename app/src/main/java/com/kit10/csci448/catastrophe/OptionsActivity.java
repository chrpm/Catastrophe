package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Host the OptionsFragment
 */
public class OptionsActivity extends SingleFragmentActivity {

    private static final String MUSIC_ON_ID =
            "com.kit10.csci448.catastrophe.music_on_id";
    private static final String SOUND_ON_ID =
            "com.kit10.csci448.catastrophe.sound_on_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    /**
     * Used to create an explicit intent to launch the OptionsActivitiy
     */
    public static Intent newIntent(Context packageContext, boolean sound, boolean music) {
        Log.d(WelcomeActivity.LOG_TAG, "OptionsActivity: new intent");
        Intent intent = new Intent(packageContext, OptionsActivity.class);
        intent.putExtra(MUSIC_ON_ID, music);
        intent.putExtra(SOUND_ON_ID, sound);
        return intent;
    }


    /**
     * Creates an OptionsFragment
     */
    @Override
    protected Fragment createFragment() {
        boolean sound = getIntent().getBooleanExtra(SOUND_ON_ID, true);
        boolean music = getIntent().getBooleanExtra(MUSIC_ON_ID, true);
        return OptionsFragment.createFragment(sound, music);
    }
}
