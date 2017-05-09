package com.kit10.csci448.catastrophe.model;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Models a kittne that dashes side to side
 */
public class ZigKitten extends TargetedKitten {
    public static final int DEFAULT_VARIABILITY = 1000;
    public static final double DEFAULT_PROBABILiTY = 0.025;

    private int centerTargetX;
    private int zigVariability;
    private double zigProbability;
    private boolean zigLeft;

    /**
     * See com.kit10.csci448.catastrophe.model.Kitten
     * @param zigVariability : defines the range on which the kitten zigzags
     * @param zigProbability : defines the likelihood off the kitten to zigzag per iteration
     */
    public ZigKitten(Bitmap sweetCatPic, int x, int y, int targetX, int targetY, Home home, double stepSize, double stepSizeGrowth, int zigVariability, double zigProbability) {
        super(sweetCatPic, x, y, targetX, targetY, home, stepSize, stepSizeGrowth);
        zigLeft = new Random().nextBoolean();
        this.zigVariability = zigVariability;
        this.zigProbability = zigProbability;
        centerTargetX = targetX;
    }

    /**
     * Overrides parent method to provide special movement
     */
    @Override
    public void flee() {
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
