package com.kit10.csci448.catastrophe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Adrien on 3/1/2017.
 */

public class OptionsFragment extends Fragment {
    public static OptionsFragment createFragment() {
        Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : new instance");

        Bundle args = new Bundle();
        OptionsFragment fragment = new OptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
