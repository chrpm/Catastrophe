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

    protected int x;
    protected int y;

    protected int targetX;
    protected int targetY;
    protected int speed;

    private Bitmap sweetCatPic;
    private boolean touched;

    public Kitten(Bitmap sweetCatPic, int x, int y, int targetX, int targetY, int speed) {
        setCoordinates(x, y);
        setTargetCoordinates(targetX, targetY);
        this.speed = speed;
        this.sweetCatPic = sweetCatPic;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sweetCatPic, x - (sweetCatPic.getWidth() / 2), y - (sweetCatPic.getHeight() / 2), null);
    }

    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth()/2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (y <= (y + sweetCatPic.getHeight() / 2))) {
                // Cat picture has been touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }
    }

    public void flee(double speed) {
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
}
