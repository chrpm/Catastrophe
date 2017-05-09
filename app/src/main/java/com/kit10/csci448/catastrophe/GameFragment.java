package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.kit10.csci448.catastrophe.model.Home;
import com.kit10.csci448.catastrophe.model.Kitten;
import com.kit10.csci448.catastrophe.model.ScoreSplash;
import com.kit10.csci448.catastrophe.model.Sound;
import com.kit10.csci448.catastrophe.model.SoundBox;
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
    public static final String TAG = "GameFragment";
    private static final String ARG_SOUND = "sound_on_id";
    private static final String ARG_MUSIC = "music_on_id";

    public int scoreMultiplier = 1;

    private SoundBox mSoundBox;
    private Sound mStartSound;
    private List<Sound> mSounds;
    private List<Sound> mHappyShortSounds;
    private List<Sound> mPurrSounds;
    private List<Sound> mUpsetSounds;
    public final double HOME_SIZE_PERCENTAGE = 0.3;
    public final int TIME_LIMIT_MINUTES = 5;
    public boolean soundOn;
    public boolean musicOn;
    private Sound mBackgroundMusic;

    private GameView mGameView;
    private ImageButton mOptionsButton;
    private Button mStartButton;
    private LinearLayout mPowerupToolbar;
    private LinearLayout mGameOverUI;
    private Button mRestartButton;
    private Sound mBuzzer;
    private Sound mBounce;
    private int backgroundID;

    private Timer mTimer;
    public static Handler mHandler;
    private TextView mTime;

    private boolean gameIsRunning = false;

    // game specific items
    private List<Kitten> mKitties;
    private long totalPlayTime = 0;
    private TextView mScore;
    private int mScoreValue;
    private boolean mTimeLimitExceeded = false;

    private Home mHome;
    private ScoreSplash mScoreSplash;
    private int mScreenWidth;
    private int mScreenHeight;

    public static GameFragment newInstance(boolean sound, boolean music) {
        Log.d(TAG, "GameFragment : new instance");
        Bundle args = new Bundle();
        args.putBoolean(ARG_SOUND, sound);
        args.putBoolean(ARG_MUSIC, music);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mSoundBox = new SoundBox(getActivity());
        soundOn = getArguments().getBoolean(ARG_SOUND);
        musicOn = getArguments().getBoolean(ARG_MUSIC);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTimer != null) {
            resumeGame();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "GameFragment : onCreateView");
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_game, container, false);



        mStartSound = mSoundBox.getStartSound();
        mPurrSounds = mSoundBox.getPurrSounds();
        mHappyShortSounds = mSoundBox.getShortHappySounds();
        mBackgroundMusic = mSoundBox.getBackgroundMusic();
        mBuzzer = mSoundBox.getBuzzer();
        mUpsetSounds = mSoundBox.getUpsetSounds();
        mBounce = mSoundBox.getBounce();

        mGameView = (GameView) v.findViewById(R.id.canvas_view);
        mKitties = new ArrayList<>();

        Bitmap splashPic = BitmapFactory.decodeResource(getResources(), R.drawable.kittensplash);
        Bitmap starPic = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        mScoreSplash = new ScoreSplash(splashPic, starPic);

        Bitmap homePic = BitmapFactory.decodeResource(getResources(), R.drawable.home); // get the home image
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        Log.d("Height", Integer.toString(mScreenHeight));
        Log.d("Width", Integer.toString(mScreenWidth));

        double homeHeight = mScreenHeight - (mScreenHeight * HOME_SIZE_PERCENTAGE);
        Log.d("Home Height", Integer.toString((int)homeHeight));
        mHome = new Home(homePic, new int[]{0,(int)homeHeight,mScreenWidth,mScreenHeight}); // home is represented as a rectangle: coordinate format is {left, right, top, bottom

        mGameView.setGameResources(mKitties, mScoreSplash, mHome);
        mGameView.setBackgroundResource(R.drawable.wooden_floor);

        mTime = (TextView)v.findViewById(R.id.time);
        mTime.setText("Time: 0:00");

        mScore = (TextView)v.findViewById(R.id.remaining);
        mScore.setText("Score: 0");

        mStartButton = (Button) v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartButton.setVisibility(View.GONE); // makes the start button invisible
                if(musicOn) {
                    backgroundID = mSoundBox.playLoop(mBackgroundMusic);
                }

                // TODO: we may want to disable everything before this button is pressed (excluding the options button)
                startNewGame();
            }
        });

        mOptionsButton = (ImageButton) v.findViewById(R.id.options_button);
        mOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "WelcomeFragment : starting options");
                mSoundBox.release();
                startActivityForResult(OptionsActivity.newIntent(getActivity()), WelcomeActivity.REQUEST_CODE_OPTIONS);
            }
        });

        mPowerupToolbar = (LinearLayout) v.findViewById(R.id.powerup_toolbar);
        mGameOverUI = (LinearLayout) v.findViewById(R.id.restart_ui);

        mRestartButton = (Button) v.findViewById(R.id.restart_button);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameOverUI.setVisibility(View.GONE);
                if(musicOn) {
                    mSoundBox.resume(backgroundID);
                }

                // TODO: Start that new game
                startNewGame();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getHandler();
    }

    public void getHandler() {
        mHandler = new Handler() {
            //the game is run on a different thread, so it has to send information to the UI thread through this handler
            public void handleMessage(Message msg) {
                Log.d("Your Message: ", Integer.toString(msg.what));
                if(msg.what == 1){
                    mTime.setText(getTime());
                    mScore.setText(getActivity().getString(R.string.score, mScoreValue));;
                } else if (msg.what == 2){
                    mGameOverUI.setVisibility(View.VISIBLE);
                }
                mGameView.update();
            }
        };
    }


    /**
     * Spawns powerups in the powerup toolbar and defines their functionality
     */
    public void populatePowerupToolbar() {
        // temporary code for demo purposes only
        mPowerupToolbar.removeAllViews();
        // TODO: implement powerups
        for(int i = 0; i < 3; i++) {
            final ImageButton powerupButton = new ImageButton(getActivity());
            powerupButton.setBackgroundResource(R.drawable.meowmix);
            powerupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scoreMultiplier = 10;
                    new CountDownTimer(5000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            scoreMultiplier = 1;
                        }
                    }.start();
                    mPowerupToolbar.removeView(powerupButton);
                }
            });
            mPowerupToolbar.addView(powerupButton);
        }
    }

    /**
     * Starts the game on a new thread that is updated at a fixed rate
     */
    private void startNewGame() {
        gameIsRunning = true;
        populatePowerupToolbar();

        totalPlayTime = 0;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }

        mTimeLimitExceeded = false;
        mScoreValue = 0;
        mKitties.clear();
        addNewKitties();

        resumeGame();
    }

    private void resumeGame() {
        mTimer = new Timer();
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
        if (minutes >= TIME_LIMIT_MINUTES) {
            mTimeLimitExceeded = true;
        }
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
                    mScreenWidth / 2, -1 * mScreenHeight / 10,
                    mHome,
                    Kitten.DEFAULT_STEP_SIZE, Kitten.DEFAULT_STEP_SIZE_GROWTH));
            mKitties.add(new ZigKitten(kittyPic,
                    mHome.centerX() + n * rand.nextInt(mHome.width() / 2), mHome.centerY() + n * rand.nextInt(mHome.height() / 2),
                    mScreenWidth / 2, -1 * mScreenHeight / 10,
                    mHome,
                    Kitten.DEFAULT_STEP_SIZE, Kitten.DEFAULT_STEP_SIZE_GROWTH,
                    ZigKitten.DEFAULT_VARIABILITY, ZigKitten.DEFAULT_PROBABILiTY));
        }
    }

    /**
     * Performs repeated game actions like moving the kittens and checking the game state
     * NOTE: this function is executed on a new thread; therefor UI changes must be forwarded through the mHandler object
     */
    private void gameLoop() {
        randomFleeing();
        Random rand = new Random();
        int kittensOnScreen = 0;
        for (Kitten k : mKitties) {
            if(k.getState() == Kitten.State.FLEEING) {
                mSoundBox.stop(k.getStreamID());
                k.resetNoise();
            }

            if(soundOn) {
                if(k.getNoise() == Kitten.Noise.PURR) {
                    int i = mSoundBox.playLoop(mPurrSounds.get(rand.nextInt(mPurrSounds.size())));
                    k.setStreamID(i);
                    k.resetNoise();
                }
                else if(k.getNoise() == Kitten.Noise.UPSET) {
                    mSoundBox.play(mUpsetSounds.get(rand.nextInt(mUpsetSounds.size())));
                    k.resetNoise();
                }

                else if(k.getNoise() == Kitten.Noise.MEOW) {
                    mSoundBox.play(mHappyShortSounds.get(rand.nextInt(mHappyShortSounds.size())));
                    k.resetNoise();
                }
                else if(k.getNoise() == Kitten.Noise.BOUNCE) {
                    mSoundBox.play(mBounce);
                    k.resetNoise();
                }
            }
            int catHeight = k.getCatHeight();
            int catWidth = k.getCatWidth();
            int screenWidth = mGameView.getWidth();
            int screenHeight = mGameView.getHeight();
            float catX = k.getX();
            float catY = k.getY();

            //Find if the kitten is on the game screen.
            if((catY + (catHeight / 2) > 0) && (catY - (catHeight / 2) < screenHeight)) {
                if((catX + (catWidth / 2) > 0) && (catX - (catWidth / 2) < screenWidth)) {
                    kittensOnScreen++;
                }
            }

            k.performMovement();

            if (k.getState() == Kitten.State.HOME) {
                if (k.getScoreStyles().size() > 0) {
                    List<String> textLines = new ArrayList<>();
                    int plusScore = 0 ;
                    for (Kitten.ScoreStyle ss : k.getScoreStyles()) {
                        Log.d(TAG, "Score style: " + ss);
                        textLines.add(ss.toString());
                        plusScore += ss.getPointValue();
                    }
                    plusScore = plusScore * scoreMultiplier;
                    mScoreValue += plusScore;
                    mScoreSplash.startDrawing(textLines, plusScore);
                }
                k.clearScoreStyles();
            }
        }

        // updates the UI
        if (gameIsRunning){
            if(kittensOnScreen == 0 || mTimeLimitExceeded){
                if(soundOn) {
                    mSoundBox.play(mBuzzer);
                }
                endGame();
            } else {
                mHandler.obtainMessage(1).sendToTarget();
            }
        }
    }

    private void endGame(){
        mSoundBox.release();
        gameIsRunning = false;
        mHandler.obtainMessage(2).sendToTarget();

        //resumeGame();
        //startNewGame();
    }


    /**
     * randomly causes kittens to flee
     */
    private void randomFleeing() {
        double fleeProbability = 0.005;
        Random rand = new Random();
        for (Kitten k : mKitties) {
            if ((k.getState() == Kitten.State.HOME) && rand.nextDouble() <= fleeProbability) {
                k.setState(Kitten.State.FLEEING);
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
            /* if(k.onScreen()) {
                count++;
            }*/
        }

        String num = new Integer(count).toString();
        String remaining = "Kittens Remaining: " + num;
        return remaining;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSoundBox.release();
    }





}
