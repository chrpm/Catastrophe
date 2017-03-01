package com.kit10.csci448.catastrophe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Adrien on 3/1/2017.
 */

public class WelcomeFragment extends Fragment {
    public static WelcomeFragment newInstance() {
        Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : new instance");
        Bundle args = new Bundle();

        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
