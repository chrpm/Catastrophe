package com.kit10.csci448.catastrophe;

import android.content.Intent;
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

    //Right now, sound and music are initially off.

public class OptionsFragment extends Fragment {

    private ImageButton mPlayButton;
    private Button mSoundOnButton;
    private Button mSoundOffButton;
    private Button mMusicOnButton;
    private Button mMusicOffButton;
    private Button mQuitButton;

    private LinearLayout mSoundOnLayout;
    private LinearLayout mSoundOffLayout;
    private LinearLayout mMusicOnLayout;
    private LinearLayout mMusicOffLayout;

    public boolean soundOn;
    public boolean musicOn;



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
        View v = inflater.inflate(R.layout.options_fragment, container, false);

        mSoundOnLayout = (LinearLayout) v.findViewById(R.id.sound_on_layout);
        mSoundOffLayout = (LinearLayout) v.findViewById(R.id.sound_off_layout);
        mMusicOnLayout = (LinearLayout) v.findViewById(R.id.music_on_layout);
        mMusicOffLayout = (LinearLayout) v.findViewById(R.id.music_off_layout);

        mPlayButton = (ImageButton) v.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : resuming game");
                startActivityForResult(GameActivity.newIntent(getActivity(), musicOn, soundOn), WelcomeActivity.REQUEST_CODE_GAME);
            }
        });

        mSoundOnButton = (Button) v.findViewById(R.id.sound_on_button);
        mSoundOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : turning sound on");
                mSoundOffLayout.setVisibility(View.VISIBLE);
                mSoundOnLayout.setVisibility(View.GONE);
                soundOn = false;
            }
        });

        mSoundOffButton = (Button) v.findViewById(R.id.sound_off_button);
        mSoundOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : turning sound off");
                mSoundOffLayout.setVisibility(View.GONE);
                mSoundOnLayout.setVisibility(View.VISIBLE);
                soundOn = true;
            }
        });

        mMusicOnButton = (Button) v.findViewById(R.id.music_on_button);
        mMusicOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : turning music on");
                mMusicOnLayout.setVisibility(View.GONE);
                mMusicOffLayout.setVisibility(View.VISIBLE);
                musicOn = false;
            }
        });

        mMusicOffButton = (Button) v.findViewById(R.id.music_off_button);
        mMusicOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : turning music off");
                mMusicOffLayout.setVisibility(View.GONE);
                mMusicOnLayout.setVisibility(View.VISIBLE);
                musicOn = true;
            }
        });

        mQuitButton = (Button) v.findViewById(R.id.quit_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : quitting");
                //android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(1);
                startActivityForResult(WelcomeActivity.newIntent(getActivity()), WelcomeActivity.REQUEST_CODE_GAME);
            }
        });

        return v;

    }
}
