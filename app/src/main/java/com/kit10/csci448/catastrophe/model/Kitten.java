package com.kit10.csci448.catastrophe.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.SyncStateContract;
import android.util.Log;

import com.kit10.csci448.catastrophe.WelcomeActivity;

import java.util.Random;

/**
 * Created by Adrien on 3/1/2017.
 */

public class Kitten {
    public static int MAX_PIXEL_X;
    public static int MAX_PIXEL_Y;

    public static final double DEFAULT_SPEED = 5.0;
    public static final double DEFAULT_SPEED_GROWTH = 0.05;

    protected int x;
    protected int y;

    protected int targetX;
    protected int targetY;
    protected Home home;
    protected double speed;
    protected double speedGrowth;

    private Bitmap sweetCatPic;
    private boolean touched;

    private boolean fleeing = false;
    private boolean escaped = false;
    private boolean scored = false;

    /**
     * @param sweetCatPic : bitmat defining the kitten's texture
     * @param x : x start location
     * @param y : y start location
     * @param targetX : x target location
     * @param targetY : y target location
     * @param home : defines the cat's home
     * @param speed : cat's move speed
     * @param speedGrowth : cat's move speed growth per game loop iteration
     */
    public Kitten(Bitmap sweetCatPic, int x, int y, int targetX, int targetY, Home home, double speed, double speedGrowth) {
        setCoordinates(x, y);
        setTargetCoordinates(targetX, targetY);
        this.home = home;
        this.speed = speed;
        this.speedGrowth = speedGrowth;
        this.sweetCatPic = sweetCatPic;
    }

    public void draw(Canvas canvas) {
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

    public void flee() {
        move();
    }

    /**
     * Moves the Kitten towards the target location
     *
     * targetX : x coordinate of target location
     * targetY : y coordinate of target location
     * speed : maximum single step move distance; this value must be large enough to prevent significant double->int truncation
     */
    public void move() {
        double hyp = Math.sqrt(Math.pow((targetX - x), 2.0) + Math.pow((targetY - y), 2.0)); // determine length of hypotenuse
        speed += speedGrowth;
        double ratio = speed / hyp;
        x += (int) (ratio * (targetX - x));
        y += (int) (ratio * (targetY - y));
    }

    public void setSweetCatPic(Bitmap sweetCatPic) {
        this.sweetCatPic = sweetCatPic;
    }
    public Bitmap getSweetCatPic() {
        return sweetCatPic;
    }


    public void setTargetCoordinates(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
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
}
