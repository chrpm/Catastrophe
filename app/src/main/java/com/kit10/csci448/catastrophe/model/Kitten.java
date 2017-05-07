package com.kit10.csci448.catastrophe.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.kit10.csci448.catastrophe.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 3/1/2017.
 */

public class Kitten {
    public static final String TAG = "Kitten";

    public static final float DEFAULT_STEP_SIZE = 5.0f;
    public static final float DEFAULT_STEP_SIZE_GROWTH = 0.001f;
    public static final float FLING_DECELLERATION = 0.2f;

    protected float x;
    protected float y;
    protected float oldX;
    protected float oldY;

    protected double velocityX;
    protected double velocityY;

    protected Home home;

    protected double stepSize;
    protected double stepSizeGrowth;

    private Bitmap sweetCatPic;

    public enum State {
      FLEEING, HELD, ESCAPED, HOME, LAUNCHED
    }
    protected State state = State.HOME;
    private int numBounces = 0;
    private boolean backboardBounce = false;

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

    public void draw(Canvas canvas) {
        if(state == State.ESCAPED){
            return;
        }
        canvas.drawBitmap(sweetCatPic, x - (sweetCatPic.getWidth() / 2), y - (sweetCatPic.getHeight() / 2), null);
    }

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
        }
    }

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
        }
    }

    public void flee() {
        move();
    }

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
            }
        }
    }

    public boolean selected(float eventX, float eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth() / 2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (eventY <= (y + sweetCatPic.getHeight() / 2))) {
                return true;
            }
        }
        return false;
    }

    public boolean atHome() {
        return (x <= home.rightX() && x >= home.leftX()) && (y <= home.bottomY() && y >= home.topY());
    }

    public void handleActionDown(float eventX, float eventY) {
        if (selected(eventX, eventY)) {
            clearScoreStyles();
            if (state == State.LAUNCHED) {
                scoreStyles.add(ScoreStyle.LAUNCH_N_CATCH);
            }
            // Cat picture has been touched
            state = State.HELD;
        }
    }

    public void handleActionUp(float eventX, float eventY) {
        if (state == State.HELD) {
            // check if the kitten is inside the home box when dropped
            if (atHome()) {
                state = State.HOME;
                scoreStyles.add(ScoreStyle.DROP);
                Log.d(TAG, "Kitten scored");
            }
            else {
                state = State.FLEEING;
            }
        }
    }

    public void handleActionFlung(float eventX, float eventY, float velocityX, float velocityY) {
        if (selected(eventX, eventY) && (state == State.FLEEING || state == State.HELD)) {
            Log.d(TAG, "Kitten flung");
            state = State.LAUNCHED;
            this.velocityX = velocityX / 500;
            this.velocityY = velocityY / 500;
        }
    }

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
}
