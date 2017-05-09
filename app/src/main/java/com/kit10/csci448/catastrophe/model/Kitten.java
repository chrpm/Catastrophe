package com.kit10.csci448.catastrophe.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.kit10.csci448.catastrophe.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the kittens in the game
 */
public class Kitten {
    public static final String TAG = "Kitten";

    public static final float DEFAULT_STEP_SIZE = 5.0f;
    public static final float DEFAULT_STEP_SIZE_GROWTH = 0.05f;
    public static final float FLING_DECELLERATION = 0.2f;

    protected float x;
    protected float y;

    protected double velocityX;
    protected double velocityY;

    protected Home home;
    private int streamID;

    protected double stepSize;
    protected double stepSizeGrowth;

    private Bitmap sweetCatPic;

    /**
     * Movement state for the kittens
     */
    public enum State {
      FLEEING, HELD, ESCAPED, HOME, LAUNCHED
    }

    /**
     * which noise to play
     */
    public enum Noise {
        PURR, UPSET, MEOW, NONE, BOUNCE
    }

    protected State state = State.HOME;
    protected Noise noise = Noise.PURR;
    private int numBounces = 0;
    private boolean backboardBounce = false;

    /**
     * Type of style used when scoring, includes point values
     */
    public enum ScoreStyle {
        DROP ("Drop", 5), LAUNCH ("Launch", 10), BACKBOARD ("Backboard", 5), BOUNCE("Bounce", 5), DOUBLE_BOUNCE("Double-bounce!", 10), MULTI_BOUNCE("Multi-Bounce!!", 15), LAUNCH_N_CATCH("Launch 'n' Catch", 5);

        public static final int MAX_SCORE = LAUNCH.getPointValue() + BACKBOARD.getPointValue() + BOUNCE.getPointValue() + MULTI_BOUNCE.getPointValue() + LAUNCH_N_CATCH.getPointValue();

        private final String name;
        private final int pointValue;

        private ScoreStyle(String s, int pointValue) {
            name = s;
            this.pointValue = pointValue;
        }

        public String toString() {
            return this.name + " +" + getPointValue();
        }

        public int getPointValue() {
            return pointValue;
        }
    }
    private List<ScoreStyle> scoreStyles;

    /**
     * @param sweetCatPic : bitmat defining the kitten's texture
     * @param x : x start location
     * @param y : y start location
     * @param home : defines the cat's home
     * @param stepSize : cat's move stepSize
     * @param stepSizeGrowth : cat's move stepSize growth per game loop iteration
     */
    public Kitten(Bitmap sweetCatPic, float x, float y, Home home, double stepSize, double stepSizeGrowth) {
        setCoordinates(x, y);
        this.home = home;
        this.stepSize = stepSize;
        this.stepSizeGrowth = stepSizeGrowth;
        this.sweetCatPic = sweetCatPic;
        this.scoreStyles = new ArrayList<>();
        setVelocities();
    }

    /**
     * draws the kitten
     */
    public void draw(Canvas canvas) {
        if(state == State.ESCAPED){
            return;
        }
        canvas.drawBitmap(sweetCatPic, x - (sweetCatPic.getWidth() / 2), y - (sweetCatPic.getHeight() / 2), null);
    }

    /**
     * decides which movement scheme to use
     */
    public void performMovement() {
        switch (state) {
            case FLEEING:
                if (scoreStyles.size() > 0) {
                    clearScoreStyles();
                }
                flee();
                break;
            case LAUNCHED:
                launched();
                break;
        }
    }

    /**
     * Moves the Kitten towards the target location
     *
     * targetX : x coordinate of target location
     * targetY : y coordinate of target location
     * stepSize : maximum single step move distance; this value must be large enough to prevent significant double->int truncation
     */
    public void move() {
        setVelocities();
        x += velocityX;
        y += velocityY;
        if (y <= -1 * getCatHeight()) {
            state = State.ESCAPED;
            Log.d(TAG, "kitten escaped");
        }
    }

    /**
     * Increase the kitten's step size
     */
    private void growStepSize() {
        stepSize *= 1 + stepSizeGrowth;
    }

    /**
     * Sets proper velocities based off position
     */
    protected void setVelocities() {
        boolean bounce = false;
        if (x <= 0) {
            velocityX = Math.abs(velocityX);
            bounce = true;
        }
        else if (x >= getScreenWidth()) {
            velocityX = -1 * Math.abs(velocityX);
            bounce = true;
        }

        if (y >= getScreenHeight()) {
            velocityY = -1 * Math.abs(velocityY);
            bounce = true;
            backboardBounce = true;
        }

        if (bounce) {
            numBounces++;
            noise = Noise.BOUNCE;
        }
    }

    /**
     * controls fleeing movment
     */
    public void flee() {
        move();
    }

    /**
     * controls launched movement
     */
    public void launched() {
        move();

        velocityX -= FLING_DECELLERATION * ((velocityX > 0) ? 1 : -1);
        velocityY -= FLING_DECELLERATION * ((velocityY > 0) ? 1 : -1);

        Log.d(TAG, String.format("velocityX: %f, velocityY: %f", velocityX, velocityY));
        if (Math.abs(velocityX) <= FLING_DECELLERATION && Math.abs(velocityY) <= FLING_DECELLERATION) {
            if (atHome()) {
                state = State.HOME;
                Log.d(TAG, "Kitten scored");

                scoreStyles.add(ScoreStyle.LAUNCH);
                if (numBounces == 1) {
                    scoreStyles.add(ScoreStyle.BOUNCE);
                }
                else if (numBounces == 2) {
                    scoreStyles.add(ScoreStyle.DOUBLE_BOUNCE);
                }
                else if (numBounces > 2) {
                    scoreStyles.add(ScoreStyle.MULTI_BOUNCE);
                }
                if (backboardBounce) {
                    scoreStyles.add(ScoreStyle.BACKBOARD);
                }
            }
            else {
                state = State.FLEEING;
                noise = Noise.NONE;
            }
        }
    }

    /**
     * @return whether the cat is selected at the provided x and y
     */
    public boolean selected(float eventX, float eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth() / 2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (eventY <= (y + sweetCatPic.getHeight() / 2))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether the cat is home
     */
    public boolean atHome() {
        return (x <= home.rightX() && x >= home.leftX()) && (y <= home.bottomY() && y >= home.topY());
    }

    /**
     * Handle what happens when a cat is touched
     */
    public void handleActionDown(float eventX, float eventY) {
        if (state == State.HOME) {
            return;
        }
        if (selected(eventX, eventY)) {
            growStepSize();
            clearScoreStyles();
            if (state == State.LAUNCHED) {
                scoreStyles.add(ScoreStyle.LAUNCH_N_CATCH);
            }
            // Cat picture has been touched
            state = State.HELD;
            noise = Noise.MEOW;
        }
    }

    /**
     * Handle what happens when a cat is released
     */
    public void handleActionUp(float eventX, float eventY) {
        if (state == State.HELD) {
            // check if the kitten is inside the home box when dropped
            if (atHome()) {
                state = State.HOME;
                noise = Noise.PURR;
                scoreStyles.add(ScoreStyle.DROP);
                Log.d(TAG, "Kitten scored");
            }
            else {
                state = State.FLEEING;
                noise = Noise.NONE;
            }
        }
    }

    /**
     * Handle what happens when a cat is flung
     */
    public void handleActionFlung(float eventX, float eventY, float velocityX, float velocityY) {
        if (selected(eventX, eventY) && (state == State.FLEEING || state == State.HELD)) {
            Log.d(TAG, "Kitten flung");
            state = State.LAUNCHED;
            noise = Noise.UPSET;
            this.velocityX = velocityX / 500;
            this.velocityY = velocityY / 500;
        }
    }

    /**
     * Clears the scoreStyles list and related attributes
     */
    public void clearScoreStyles() {
        scoreStyles.clear();
        numBounces = 0;
        backboardBounce = false;
    }

    public void setSweetCatPic(Bitmap sweetCatPic) {
        this.sweetCatPic = sweetCatPic;
    }
    public Bitmap getSweetCatPic() {
        return sweetCatPic;
    }

    public void setCoordinates(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setRelativeX(double relativeX) {
        this.x = (int) (getScreenWidth() * relativeX);
    }
    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }
    public void setRelativeY(double relativeY) {
        this.y = (int) (getScreenHeight() * relativeY);
    }
    public float getY() {
        return y;
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getCatWidth() {
        return sweetCatPic.getWidth();
    }

    public int getCatHeight() {
        return sweetCatPic.getHeight();
    }

    public List<ScoreStyle> getScoreStyles() {
        return scoreStyles;
    }

    public Noise getNoise() {return noise;}

    public void resetNoise() {noise = Noise.NONE;}

    public void setStreamID(int id) {streamID = id;}

    public int getStreamID() {return streamID;}
}
