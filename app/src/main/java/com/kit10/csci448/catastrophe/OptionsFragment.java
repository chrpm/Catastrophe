package com.kit10.csci448.catastrophe;

import android.content.Context;
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

import static android.app.Activity.RESULT_OK;

/**
 * Created by Adrien on 3/1/2017.
 */

//To take screenshot onClickListener to overlay options onto paused game screen.
//http://stackoverflow.com/questions/3733988/screen-capture-in-android

    //Right now, sound and music are initially off.

public class OptionsFragment extends Fragment {

    private static final String EXTRA_SOUND =
            "com.kit10.csci448.catastrophe.sound_on_id";
    private static final String EXTRA_MUSIC =
            "com.kit10.csci448.catastrophe.music_on_id";
    private static final String ARG_SOUND = "sound_on_id";
    private static final String ARG_MUSIC = "music_on_id";


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


    private void setAudioResult(boolean sound, boolean music) {
        Intent result = new Intent();
        result.putExtra(EXTRA_SOUND, sound);
        result.putExtra(EXTRA_MUSIC, music);
        getActivity().setResult(RESULT_OK, result);
    }

    public static OptionsFragment createFragment(boolean sound, boolean music) {
        Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : new instance");

        Bundle args = new Bundle();
        args.putBoolean(ARG_SOUND, sound);
        args.putBoolean(ARG_MUSIC, music);
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

        soundOn = getArguments().getBoolean(ARG_SOUND);
        musicOn = getArguments().getBoolean(ARG_MUSIC);
        if(soundOn) {
            mSoundOnLayout.setVisibility(View.VISIBLE);
            mSoundOffLayout.setVisibility(View.GONE);
        }
        else {
            mSoundOnLayout.setVisibility(View.GONE);
            mSoundOffLayout.setVisibility(View.VISIBLE);
        }
        if(musicOn) {
            mMusicOnLayout.setVisibility(View.VISIBLE);
            mMusicOffLayout.setVisibility(View.GONE);
        }
        else {
            mMusicOnLayout.setVisibility(View.GONE);
            mMusicOffLayout.setVisibility(View.VISIBLE);
        }

        mPlayButton = (ImageButton) v.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "OptionsFragment : resuming game");
                setAudioResult(soundOn, musicOn);
                getActivity().finish();
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
                startActivityForResult(WelcomeActivity.newIntent(getActivity(), soundOn, musicOn), WelcomeActivity.REQUEST_CODE_GAME);
            }
        });

        return v;

    }
}
