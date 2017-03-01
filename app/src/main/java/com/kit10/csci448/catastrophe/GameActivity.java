package com.kit10.csci448.catastrophe;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance();
    }
}
