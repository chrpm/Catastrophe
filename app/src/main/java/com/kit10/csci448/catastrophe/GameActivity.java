package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GameActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        Log.d(WelcomeActivity.LOG_TAG, "GameActivity: new intent");
        Intent intent = new Intent(packageContext, GameActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance();
    }
}
