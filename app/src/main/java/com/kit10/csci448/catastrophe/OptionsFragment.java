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

//To take screenshot onClickListener to overlay options onto paused game screen.
//http://stackoverflow.com/questions/3733988/screen-capture-in-android

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
        //return super.onCreateView(inflater, container, savedInstanceState);
        // TODO: create layout and implement this function
        View v = inflater.inflate(R.layout.options_fragment, container, false);
        return v;

    }
}
