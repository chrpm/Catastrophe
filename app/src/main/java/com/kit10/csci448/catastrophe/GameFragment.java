package com.kit10.csci448.catastrophe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kit10.csci448.catastrophe.model.Home;
import com.kit10.csci448.catastrophe.model.Kitten;
import com.kit10.csci448.catastrophe.model.TargetedKitten;
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
    public final double HOME_SIZE_PERCENTAGE = 0.3;

    private GameView mGameView;
    private ImageButton mOptionsButton;
    private Button mStartButton;
    private LinearLayout mPowerupToolbar;
    private Boolean play;
    private int kittensRemaining = 0;
    private String string = "";
    private String str = "";

    private Timer mTimer;
    private TimerTask mTask;
    public Handler mHandler;
    private long startTime;
    private long totalPlayTime = 0;
    private TextView mTime;
    private TextView mRemaining;

    int i = 0;

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
        Bitmap homePic = BitmapFactory.decodeResource(getResources(), R.drawable.home); // get the home image
        /*
        TODO: soft-code this line;
        TODO: the home should be placed at the bottom center of the screen, regardless of hardware/screen size
        TODO: the home's dimensions should reflect the size of the bitmap
        */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        Log.d("Height", Integer.toString(screenHeight));
        Log.d("Width", Integer.toString(screenWidth));

        double homeHeight = screenHeight - (screenHeight * HOME_SIZE_PERCENTAGE);
        Log.d("Home Height", Integer.toString((int)homeHeight));

        mHome = new Home(homePic, new int[]{0,(int)homeHeight,screenWidth,screenHeight}); // home is represented as a rectangle: coordinate format is {left, right, top, bottom

        mGameView.setGamePieces(mKitties, mHome);

        mTime = (TextView)v.findViewById(R.id.time);
        mTime.setText("Time: 0:00");

        mRemaining = (TextView)v.findViewById(R.id.remaining);
        mRemaining.setText("Kittens Remaining: 0");


        mStartButton = (Button) v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartButton.setVisibility(View.GONE); // makes the start button invisible
                // TODO: we may want to disable everything before this button is pressed (excluding the options button)
                startGame();
            }
        });
        mHandler = new Handler() {
             //the game is run on a different thread, so it has to send information to the UI thread through this handler
            public void handleMessage(Message msg) {
                mTime.setText(getTime());
                mRemaining.setText(recountKitties());
                mRemaining.setText(string);
                //mTime.setText(str);
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

    /**
     * Spawns powerups in the powerup toolbar and defines their functionality
     */
    public void populatePowerupToolbar() {
        // temporary code for demo purposes only
        // TODO: implement powerups
        for(int i = 0; i < 3; i++) {
            final ImageButton powerupButton = new ImageButton(getActivity());
            powerupButton.setBackgroundResource(R.drawable.meowmix);
            powerupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: make kittens run back towards home
                    Toast.makeText(getActivity(), "Powerup Selected!", Toast.LENGTH_SHORT).show();
                    mPowerupToolbar.removeView(powerupButton);
                }
            });
            mPowerupToolbar.addView(powerupButton);
        }
    }

    /**
     * Starts the game on a new thread that is updated at a fixed rate
     */
    private void startGame() {
        mTimer = new Timer();
        addNewKitties();
        int updateRate = 10; // update the game every 10 ms
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                gameLoop();
            }
        }, 0, updateRate);
    }

    private String getTime() {
        int seconds = (int) (totalPlayTime / 1000);
        totalPlayTime = totalPlayTime + 10;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String sSeconds;
        String sMinutes;
        if(seconds < 10) {
            sSeconds = new Integer(seconds).toString();
            sSeconds = "0" + sSeconds;
            sMinutes = new Integer(minutes).toString();
        }
        else {
            sSeconds = new Integer(seconds).toString();
            sMinutes = new Integer(minutes).toString();
        }
        String time = "Time: " + sMinutes + ":" + sSeconds;
        return time;


    }

    /**
     * Generates kittens in the home box
     */
    private void addNewKitties() {
        Random rand = new Random();
        Bitmap kittyPic = BitmapFactory.decodeResource(getResources(), R.drawable.cool_cat);
        for (int i = 0; i < 3; i++) { // TODO: generate kittens on a per-level basis
            int n = rand.nextBoolean() ? -1 : 1; // sets n to either 1 or -1
            mKitties.add(new TargetedKitten(kittyPic,
                    mHome.centerX() + n * rand.nextInt(mHome.width() / 2), mHome.centerY() + n * rand.nextInt(mHome.height() / 2),
                    700, 0,
                    mHome,
                    Kitten.DEFAULT_SPEED, Kitten.DEFAULT_SPEED_GROWTH));
            kittensRemaining++;
            mKitties.add(new ZigKitten(kittyPic,
                    mHome.centerX() + n * rand.nextInt(mHome.width() / 2), mHome.centerY() + n * rand.nextInt(mHome.height() / 2),
                    700, 0,
                    mHome,
                    Kitten.DEFAULT_SPEED, Kitten.DEFAULT_SPEED_GROWTH,
                    ZigKitten.DEFAULT_VARIABILITY, ZigKitten.DEFAULT_PROBABILiTY));
            kittensRemaining++;
        }
    }

    /**
     * Performs repeated game actions like moving the kittens and checking the game state
     * NOTE: this function is executed on a new thread; therefor UI changes must be forwarded through the mHandler object
     */
    private void gameLoop() {
        randomFleeing();
        for (Kitten k : mKitties) {
            int catHeight = k.getCatHeight();
            int catWidth = k.getCatWidth();
            int screenWidth = mGameView.getWidth();
            int screenHeight = mGameView.getHeight();
            int catX = k.getX();
            int catY = k.getY();
            String hits = new Integer(k.getHitsLeft()).toString();
            string = hits;

            //Find if the kitten is on the game screen.
            if((catY + (catHeight / 2) > 0) && (catY - (catHeight / 2) < screenHeight)) {
                if((catX + (catWidth / 2) > 0) && (catX - (catWidth / 2) < screenWidth)) {
                    k.setOnScreen(true);
                    //string = "On Screen";
                }
                else {
                    k.setOnScreen(false);
                    //string = "Not On Screen";
                }
            }
            else {
                k.setOnScreen(false);
                //string = "Not On Screen";
            }

            if (k.isFleeing()) {
                k.flee();
            }

            if (k.isScored()) {
                // TODO: update the UI
            }
        }

        // updates the UI
        mHandler.obtainMessage(1).sendToTarget();
    }

    /**
     * randomly causes kittens to flee
     */
    private void randomFleeing() {
        double fleeProbability = 0.005;
        Random rand = new Random();
        for (Kitten k : mKitties) {
            if (rand.nextDouble() <= fleeProbability) {
                k.setFleeing(true);
            }
        }
    }

    public void resetPlayTime() {
        totalPlayTime = 0;
    }

    public void incrementPlayTime() {
        totalPlayTime = totalPlayTime + 10;
    }

    public long getPlayTime() {
        return totalPlayTime;
    }

    public String recountKitties() {
        int count = 0;
        for(Kitten k : mKitties) {
            if(k.onScreen()) {
                count++;
            }
        }

        String num = new Integer(count).toString();
        String remaining = "Kittens Remaining: " + num;
        return remaining;
    }
}
