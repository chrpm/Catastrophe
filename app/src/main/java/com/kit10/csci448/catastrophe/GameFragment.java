package com.kit10.csci448.catastrophe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.kit10.csci448.catastrophe.model.Home;
import com.kit10.csci448.catastrophe.model.Kitten;
import com.kit10.csci448.catastrophe.model.ZigKitten;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private Home mHome;

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
        mKitties = new ArrayList<>();
        Bitmap homePic = BitmapFactory.decodeResource(getResources(), R.drawable.home);
        mHome = new Home(homePic, new int[]{200,1200,1600,2000});
        mGameView.setGamePieces(mKitties, mHome);

        mStartButton = (Button) v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartButton.setVisibility(View.GONE);
                startGame();
            }
        });
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                mGameView.update();
            }
        };

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

    private void startGame() {
        mTimer = new Timer();
        addNewKitties();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            gameLoop();
            }
        }, 0, 10);
    }

    private void addNewKitties() {
        Random rand = new Random();
        Bitmap kittyPic = BitmapFactory.decodeResource(getResources(), R.drawable.cool_cat);
        for (int i = 0; i < 3; i++) { // TODO: generate kittens on a per-level basis
            int n = rand.nextBoolean() ? -1 : 1; // sets n to either 1 or -1
            mKitties.add(new Kitten(kittyPic,
                    mHome.centerX() + n * rand.nextInt(mHome.width() / 2), mHome.centerY() + n * rand.nextInt(mHome.height() / 2),
                    700, 0,
                    mHome,
                    Kitten.DEFAULT_SPEED, Kitten.DEFAULT_SPEED_GROWTH));
            mKitties.add(new ZigKitten(kittyPic,
                    mHome.centerX() + n * rand.nextInt(mHome.width() / 2), mHome.centerY() + n * rand.nextInt(mHome.height() / 2),
                    700, 0,
                    mHome,
                    Kitten.DEFAULT_SPEED, Kitten.DEFAULT_SPEED_GROWTH,
                    ZigKitten.DEFAULT_VARIABILITY, ZigKitten.DEFAULT_PROBABILiTY));
        }
    }

    private void gameLoop() {
        randomFleeing();
        for (Kitten k : mKitties) {
            if (k.isFleeing()) {
                k.flee();
            }
            if (k.getY() <= GameView.UPPER_BORDER) {
                k.setEscaped(true);
                k.setFleeing(false);
            }
        }

        // updates the UI
        mHandler.obtainMessage(1).sendToTarget();
    }

    private void randomFleeing() {
        double fleeProbability = 0.005;
        Random rand = new Random();
        for (Kitten k : mKitties) {
            if (rand.nextDouble() <= fleeProbability) {
                k.setFleeing(true);
            }
        }
    }
}
