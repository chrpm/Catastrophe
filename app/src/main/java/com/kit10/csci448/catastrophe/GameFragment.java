package com.kit10.csci448.catastrophe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Adrien on 3/1/2017.
 */

public class GameFragment extends Fragment {
    private GameView mGameView;
    private ImageButton mOptionsButton;
    private Button mStartButton;
    private LinearLayout mPowerupToolbar;

    private Timer mTimer;
    public Handler mHandler;

    private List<Kitten> mKitties;

    public static GameFragment newInstance() {
        Log.d(WelcomeActivity.LOG_TAG, "GameFragment : new instance");
        Bundle args = new Bundle();

        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(WelcomeActivity.LOG_TAG, "GameFragment : onCreateView");
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_game, container, false);

        mGameView = (GameView) v.findViewById(R.id.canvas_view);
        Bitmap kittyPic = BitmapFactory.decodeResource(getResources(), R.drawable.cool_cat);
        mKitties = new ArrayList<>();
        mKitties.add(new Kitten(kittyPic, 400, 400));
        mKitties.add(new Kitten(kittyPic, 200, 200));
        mGameView.setKitties(mKitties);

        mStartButton = (Button) v.findViewById(R.id.start_button);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                mGameView.update();
            }
        };
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartButton.setVisibility(View.GONE);
                startGame();
            }
        });

        mOptionsButton = (ImageButton) v.findViewById(R.id.options_button);
        mOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : starting options");
                startActivityForResult(OptionsActivity.newIntent(getActivity()), WelcomeActivity.REQUEST_CODE_OPTIONS);
            }
        });

        mPowerupToolbar = (LinearLayout) v.findViewById(R.id.powerup_toolbar);
        populatePowerupToolbar();

        return v;
    }

    public void populatePowerupToolbar() {
        // temporary code for demo purposes only
        for(int i = 0; i < 3; i++) {
            final ImageButton powerupButton = new ImageButton(getActivity());
            powerupButton.setBackgroundResource(R.drawable.meowmix);
            powerupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Powerup Selected!", Toast.LENGTH_SHORT).show();
                    mPowerupToolbar.removeView(powerupButton);
                }
            });
            mPowerupToolbar.addView(powerupButton);
        }
    }

    public void startGame() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (Kitten k : mKitties) {
                    k.flee();
                }
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }
}
