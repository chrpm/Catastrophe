package com.kit10.csci448.catastrophe.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Displays score animations and text
 */
public class ScoreSplash {
    public static final int DRAW_TIME = 100;
    private static final int MAX_STARS = 20;

    private Paint textPaint;
    private Paint scorePaint;

    private List<Star> stars;
    private int starsToDraw = MAX_STARS;
    private Bitmap splashPic;
    private Bitmap starPic;

    private List<String> textLines;
    private int plusScore;

    private boolean drawing;
    private int drawTime;

    private float x;
    private float y;

    /**
     * @param splashPic : image asset for the splash background
     * @param starPic : image asset for the star
     */
    public ScoreSplash(Bitmap splashPic, Bitmap starPic) {
        this.splashPic = splashPic;
        this.starPic = starPic;

        x = (getScreenWidth() - splashPic.getWidth()) / 2;
        y = splashPic.getWidth() / 8;

        drawing = false;

        stars = new ArrayList<>();
        for (int i = 0; i < MAX_STARS; i++) {
            stars.add(new Star(x + splashPic.getWidth() / 2, y + splashPic.getHeight() / 2));
        }

        textPaint = new Paint();
        textPaint.setTypeface(Typeface.create("Impact",Typeface.NORMAL));
        textPaint.setTextSize(getScreenHeight() / 20);
        textPaint.setColor(Color.WHITE);

        scorePaint = new Paint();
        scorePaint.setTypeface(Typeface.create("Impact",Typeface.BOLD));
        scorePaint.setTextSize(getScreenHeight() / 10);
        scorePaint.setColor(Color.YELLOW);
    }

    /**
     * Draws the ScoreSplash
     */
    public void draw(Canvas canvas) {
        if (plusScore > 20) {
            canvas.drawBitmap(splashPic, x, y, null);
        }
        drawStars(canvas);
        if (textLines != null) {
            drawText(canvas);
        }
        drawTime--;
        if (drawTime <= 0) {
            drawing = false;
            plusScore = 0;
            textLines = null;
        }
    }

    /**
     * Draws and animates the stars
     */
    public void drawStars(Canvas canvas) {
        int starsDrawn = 0;
        for (Star star : stars) {
            star.move();
            canvas.drawBitmap(starPic, star.x, star.y, null);

            starsDrawn++;
            if (starsDrawn >= starsToDraw) {
                break;
            }
        }
    }

    /**
     * Draws the text
     */
    public void drawText(Canvas canvas) {
        float textX = 0;
        float textY = y + textPaint.getTextSize();
        for (String line : textLines) {
            canvas.drawText(line, textX, textY, textPaint);
            textY += textPaint.getTextSize();
        }
        String scoreString = "+" + plusScore;
        canvas.drawText(scoreString,
                x + splashPic.getWidth() / 2 - (scorePaint.getTextSize() * scoreString.length()) / 3,
                y + splashPic.getHeight() / 2 + scorePaint.getTextSize() / 2,
                scorePaint);
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * @return whether or not to display the splashcscore
     */
    public boolean drawing() {
        return drawing;
    }

    /**
     * Starts drawing the ScoreSplash
     */
    public void startDrawing(List<String> textLines, int plusScore) {
        drawTime = DRAW_TIME;
        drawing = true;

        for (Star s : stars) {
            s.initAttributes(x + splashPic.getWidth() / 2, y + splashPic.getHeight() / 2);
            // Log.d("ScoreSplash", String.format("star x: %f, star y: %f", s.x, s.y));
        }
        this.textLines = textLines;
        this.plusScore = plusScore;

        starsToDraw = (int) ((float) plusScore / (float) Kitten.ScoreStyle.MAX_SCORE * MAX_STARS);
        Log.d("ScoreSplash", String.format("plusScore: %d, maxScore: %d, starsToDraw: %d", plusScore, Kitten.ScoreStyle.MAX_SCORE, starsToDraw));
    }

    /**
     * Animated stars that explode from the ScoreSplash
     */
    private class Star {
        public static final float MAX_VELOCITY = 10f;

        public float x;
        public float y;

        public float velocityX;
        public float velocityY;

        public Star(float x, float y) {
            initAttributes(x, y);
        }

        public void initAttributes(float x, float y) {
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
