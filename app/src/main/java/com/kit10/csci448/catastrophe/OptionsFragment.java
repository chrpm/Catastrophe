package com.kit10.csci448.catastrophe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by Adrien on 3/1/2017.
 */

//To take screenshot onClickListener to overlay options onto paused game screen.
//http://stackoverflow.com/questions/3733988/screen-capture-in-android

public class OptionsFragment extends Fragment {

    private ImageButton mPlayButton;
    private Button mSoundOnButton;
    private Button mSoundOffButton;

    private LinearLayout mSoundOnLayout;
    private LinearLayout mSoundOffLayout;

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

        mSoundOnLayout = (LinearLayout) v.findViewById(R.id.sound_on_layout);
        mSoundOffLayout = (LinearLayout) v.findViewById(R.id.sound_off_layout);

        mPlayButton = (ImageButton) v.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : resuming game");
                startActivityForResult(GameActivity.newIntent(getActivity()), WelcomeActivity.REQUEST_CODE_GAME);
            }
        });

        mSoundOnButton = (Button) v.findViewById(R.id.sound_on_button);
        mSoundOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : turning sound on");
                mSoundOffLayout.setVisibility(View.GONE);
                mSoundOnLayout.setVisibility(View.VISIBLE);
            }
        });

        mSoundOffButton = (Button) v.findViewById(R.id.sound_off_button);
        mSoundOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : turning sound off");
                mSoundOnLayout.setVisibility(View.GONE);
                mSoundOffLayout.setVisibility(View.VISIBLE);
            }
        });

        return v;

    }
}
