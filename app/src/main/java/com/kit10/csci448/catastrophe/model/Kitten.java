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

    protected int x;
    protected int y;
    protected int oldX;
    protected int oldY;

    protected double velocityX;
    protected double velocityY;

    protected Home home;

    protected double stepSize;
    protected double stepSizeGrowth;

    private Bitmap sweetCatPic;
    private boolean touched;

    private boolean fleeing = false;
    private boolean escaped = false;
    private boolean scored = false;
    private boolean onScreen = false;

    private int hitsLeft;

    /**
     * @param sweetCatPic : bitmat defining the kitten's texture
     * @param x : x start location
     * @param y : y start location
     * @param home : defines the cat's home
     * @param stepSize : cat's move stepSize
     * @param stepSizeGrowth : cat's move stepSize growth per game loop iteration
     */
    public Kitten(Bitmap sweetCatPic, int x, int y, Home home, double stepSize, double stepSizeGrowth) {
        setCoordinates(x, y);
        this.home = home;
        this.stepSize = stepSize;
        this.stepSizeGrowth = stepSizeGrowth;
        this.sweetCatPic = sweetCatPic;
        hitsLeft = 3;
        setVelocities();
    }

    public void draw(Canvas canvas) {
        if(isEscaped()){
            return;
        }
        canvas.drawBitmap(sweetCatPic, x - (sweetCatPic.getWidth() / 2), y - (sweetCatPic.getHeight() / 2), null);
    }

    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth()/2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (y <= (y + sweetCatPic.getHeight() / 2)) && fleeing) {
                // Cat picture has been touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }
    }

    public void handleActionUp(int eventX, int eventY) {
        if (touched) {
            // check if the kitten is inside the home box when dropped
            if ((eventX <= home.rightX() && eventX >= home.leftX()) && (eventY <= home.bottomY() && eventY >= home.topY())) {
                fleeing = false;
                scored = true;
                Log.d(WelcomeActivity.LOG_TAG, "Kitten scored");
            }
        }
    }

    public void handleActionFlung(float eventX, float eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth()/2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (y <= (y + sweetCatPic.getHeight() / 2)) && fleeing) {
                fleeing = false;
                scored = true;
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
            setEscaped(true);
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

    public void setCoordinates(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setRelativeX(double relativeX) {
        this.x = (int) (MAX_PIXEL_X * relativeX);
    }
    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void setRelativeY(double relativeY) {
        this.y = (int) (MAX_PIXEL_Y * relativeY);
    }
    public int getY() {
        return y;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
    public boolean isTouched() {
        return touched;
    }

    public boolean isFleeing() {
        return fleeing;
    }
    public void setFleeing(boolean fleeing) {
        this.fleeing = fleeing;
    }

    public boolean isEscaped() {
        return escaped;
    }
    public void setEscaped(boolean escaped) {
        this.escaped = escaped;
    }

    public boolean isScored() {
        return scored;
    }
    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int getCatWidth() {
        return sweetCatPic.getWidth();
    }

    public int getCatHeight() {
        return sweetCatPic.getHeight();
    }

    public int getHitsLeft() {
        return hitsLeft;
    }

    public void hit() {
        hitsLeft = hitsLeft - 1;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public boolean onScreen() {
        return onScreen;
    }

    public void setOnScreen(boolean on) {
        onScreen = on;
    }
}
