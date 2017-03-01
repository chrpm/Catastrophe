package com.kit10.csci448.catastrophe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
        /* TODO: create layout and implement this function
        View v = inflater.inflate(R.layout.fragment_options, container, false);
        return v;
         */
    }
}
