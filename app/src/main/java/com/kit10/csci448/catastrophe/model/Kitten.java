package com.kit10.csci448.catastrophe.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.kit10.csci448.catastrophe.WelcomeActivity;

/**
 * Created by Adrien on 3/1/2017.
 */

public class Kitten {
    public static int MAX_PIXEL_X;
    public static int MAX_PIXEL_Y;

    public static final double DEFAULT_STEP_SIZE = 5.0;
    public static final double DEFAULT_STEP_SIZE_GROWTH = 0.005;

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
      FLEEING, HELD, ESCAPED, SCORED
    };

    private State state;

    private int hitsLeft;

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
        hitsLeft = 3;
        setVelocities();
    }

    public void draw(Canvas canvas) {
        if(state == State.ESCAPED){
            return;
        }
        canvas.drawBitmap(sweetCatPic, x - (sweetCatPic.getWidth() / 2), y - (sweetCatPic.getHeight() / 2), null);
    }

    public void handleActionDown(float eventX, float eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth()/2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (y <= (y + sweetCatPic.getHeight() / 2)) && state == State.FLEEING) {
                // Cat picture has been touched
                state = State.HELD;
            } else {
                state = State.FLEEING;
            }
        }
    }

    public void handleActionUp(float eventX, float eventY) {
        if (state == State.HELD) {
            // check if the kitten is inside the home box when dropped
            if ((eventX <= home.rightX() && eventX >= home.leftX()) && (eventY <= home.bottomY() && eventY >= home.topY())) {
                state = State.SCORED;
                Log.d(WelcomeActivity.LOG_TAG, "Kitten scored");
            }
        }
    }

    public void handleActionFlung(float eventX, float eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth()/2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (y <= (y + sweetCatPic.getHeight() / 2)) && state == State.FLEEING) {
                state = State.SCORED;
                setY(home.centerY());
                Log.d(WelcomeActivity.LOG_TAG, "Kitten scored");
            }
        }
    }

    public void flee() {
        move();
    }

    /**
     * Moves the Kitten towards the target location
     *
     * targetX : x coordinate of target location
     * targetY : y coordinate of target location
     * stepSize : maximum single step move distance; this value must be large enough to prevent significant double->int truncation
     */
    public void move() {
        stepSize *= 1 + stepSizeGrowth;
        setVelocities();
        x += velocityX;
        y += velocityY;
        if (y <= 0) {
            state = State.ESCAPED;
        }
    }

    protected void setVelocities() {

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
        this.x = (int) (MAX_PIXEL_X * relativeX);
    }
    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }
    public void setRelativeY(double relativeY) {
        this.y = (int) (MAX_PIXEL_Y * relativeY);
    }
    public float getY() {
        return y;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
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
}
