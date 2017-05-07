package com.kit10.csci448.catastrophe.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Adrien on 5/7/2017.
 */

public class ScoreSplash {
    public static final int DRAW_TIME = 100;
    private static final int NUM_STARS = 10;

    private List<String> textList;
    private List<Star> stars;
    private Bitmap splashPic;
    private Bitmap starPic;
    private boolean drawing;
    private int drawTime;

    private float x;
    private float y;

    public ScoreSplash(Bitmap splashPic, Bitmap starPic) {
        this.splashPic = splashPic;
        this.starPic = starPic;
        x = (getScreenWidth() - splashPic.getWidth()) / 2;
        y = splashPic.getWidth() / 8;
        stars = new ArrayList<>();
        drawing = false;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(splashPic, x, y, null);
        drawStars(canvas);
        drawTime--;
        if (drawTime <= 0) {
            drawing = false;
        }
    }

    public void drawStars(Canvas canvas) {
        for (Star star : stars) {
            star.move();
            canvas.drawBitmap(starPic, star.x, star.y, null);
        }
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    public boolean drawing() {
        return drawing;
    }

    public void startDrawing() {
        drawTime = DRAW_TIME;
        drawing = true;
        stars.clear();
        for (int i = 0; i < NUM_STARS; i++) {
            stars.add(new Star(this.x + splashPic.getWidth() / 2, this.y + splashPic.getHeight() / 2));
        }
    }

    private class Star {
        public static final float MAX_VELOCITY = 10f;

        public float x;
        public float y;

        public float velocityX;
        public float velocityY;

        public Star(float x, float y) {
            this.x = x;
            this.y = y;
            Random rand = new Random();

            velocityX = (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * MAX_VELOCITY;
            velocityY = (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * MAX_VELOCITY;
        }

        public void move() {
            x += velocityX;
            y += velocityY;
        }
    }
}
