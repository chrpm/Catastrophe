package com.kit10.csci448.catastrophe.model;

import android.graphics.Bitmap;

/**
 * Created by ccollier on 4/7/2017.
 */

public class TargetedKitten extends Kitten {
    protected int targetX;
    protected int targetY;

    /**
     * @param sweetCatPic : bitmat defining the kitten's texture
     * @param x           : x start location
     * @param y           : y start location
     * @param targetX     : x target location
     * @param targetY     : y target location
     * @param home        : defines the cat's home
     * @param speed       : cat's move speed
     * @param speedGrowth : cat's move speed growth per game loop iteration
     */
    public TargetedKitten(Bitmap sweetCatPic, int x, int y, int targetX, int targetY, Home home, double speed, double speedGrowth) {
        super(sweetCatPic, x, y, home, speed, speedGrowth);
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void move() {
        oldX = x;
        oldY = y;
        double hyp = Math.sqrt(Math.pow((targetX - x), 2.0) + Math.pow((targetY - y), 2.0)); // determine length of hypotenuse
        speed += speedGrowth;
        double ratio = speed / hyp;
        x += (int) (ratio * (targetX - x));
        y += (int) (ratio * (targetY - y));
    }

    public void setTargetCoordinates(int targetX, int targetY) {
        setTargetX(targetX);
        setTargetY(targetY);
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetX(int x) {
        targetX = x;
    }

    public void setTargetY(int y) {
        targetY = y;
    }
}
