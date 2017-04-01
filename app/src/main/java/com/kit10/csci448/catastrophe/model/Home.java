package com.kit10.csci448.catastrophe.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrien on 3/31/2017.
 */

public class Home {
    private enum CoordinateId {
        LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y
    }

    private Map<CoordinateId, Integer> coordinateMap;
    private Bitmap sweetHomePic;

    /**
     * @param sweetHomePic : bitmat defining the home's texture
     * @param coordinates : defines the home's boundaries, format is {LEFT, TOP, RIGHT, BOTTOM}
     */
    public Home(Bitmap sweetHomePic, int[] coordinates) {
        this.sweetHomePic = sweetHomePic;
        coordinateMap = new HashMap<>();
        coordinateMap.put(CoordinateId.LEFT_X, coordinates[0]);
        coordinateMap.put(CoordinateId.TOP_Y, coordinates[1]);
        coordinateMap.put(CoordinateId.RIGHT_X, coordinates[2]);
        coordinateMap.put(CoordinateId.BOTTOM_Y, coordinates[3]);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sweetHomePic, leftX(), topY(), null);
    }

    public int leftX() {
        return coordinateMap.get(CoordinateId.LEFT_X);
    }
    public int rightX() {
        return coordinateMap.get(CoordinateId.RIGHT_X);
    }
    public int topY() {
        return coordinateMap.get(CoordinateId.TOP_Y);
    }
    public int bottomY() {
        return coordinateMap.get(CoordinateId.BOTTOM_Y);
    }

    public int centerX() {
        return (leftX() + rightX()) / 2;
    }
    public int centerY() {
        return (topY() + bottomY()) / 2;
    }

    public int width() {
        return rightX() - leftX();
    }
    public int height() {
        return bottomY() - topY();
    }
}
