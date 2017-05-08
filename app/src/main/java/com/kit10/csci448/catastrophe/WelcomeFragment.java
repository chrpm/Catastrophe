package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.kit10.csci448.catastrophe.model.SoundBox;

/**
 * Created by Adrien on 3/1/2017.
 */

public class WelcomeFragment extends Fragment {

    private ImageButton mStartButton;
    private static final String ARG_SOUND = "sound_on_id";
    private static final String ARG_MUSIC = "music_on_id";
    public boolean sound;
    public boolean music;

    public static WelcomeFragment newInstance(boolean sound, boolean music) {
        Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : new instance");
        Bundle args = new Bundle();
        args.putBoolean(ARG_SOUND, sound);
        args.putBoolean(ARG_MUSIC, music);
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        sound = getArguments().getBoolean(ARG_SOUND);
        music = getArguments().getBoolean(ARG_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : onCreateView");
        View v = inflater.inflate(R.layout.welcome_fragment, container, false);

        mStartButton = (ImageButton) v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : starting game");
                startActivityForResult(GameActivity.newIntent(getActivity(), sound, music), WelcomeActivity.REQUEST_CODE_GAME);
            }
        });

        return v;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }
}
