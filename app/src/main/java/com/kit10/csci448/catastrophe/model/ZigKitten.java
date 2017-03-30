package com.kit10.csci448.catastrophe.model;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by Adrien on 3/30/2017.
 */

public class ZigKitten extends Kitten {
    private int centerTargetX;
    private int zigVariability;
    private double zigProbability;
    private boolean zigLeft;

    public ZigKitten(Bitmap sweetCatPic, int x, int y, int targetX, int targetY, int speed, int zigVariability, double zigProbability) {
        super(sweetCatPic, x, y, targetX, targetY, speed);
        zigLeft = new Random().nextBoolean();
        this.zigVariability = zigVariability;
        this.zigProbability = zigProbability;
        centerTargetX = targetX;
    }

    @Override
    public void move() {
        if (new Random().nextDouble() <= zigProbability) {
            zigLeft = !zigLeft;
        }
        if (zigLeft) {
            targetX = centerTargetX - zigVariability;
        }
        else {
            targetX = centerTargetX + zigVariability;
        }

        super.move();
    }
}