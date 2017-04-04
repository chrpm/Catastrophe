package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.kit10.csci448.catastrophe.model.Home;
import com.kit10.csci448.catastrophe.model.Kitten;

import java.util.List;

/**
 * Created with help from JavaCodeGeeks CanvasView example
 */
public class GameView extends View {
    private Context context;
    public int width;
    public int height;
    public static final int UPPER_BORDER = 0;
    private GestureDetectorCompat mDetector;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private List<Kitten> mKitties;

    private Home mHome;

    public GameView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        mDetector = new GestureDetectorCompat(c,new GameGestureListener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = w;
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHome.draw(canvas);
        for (Kitten k : mKitties) {
            k.draw(canvas);
        }
    }

    public void update() {
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Kitten k : mKitties) {
                    k.handleActionDown((int)x, (int)y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                for (Kitten k : mKitties) {
                    if (k.isTouched()) {
                        // kitten is being moved
                        k.setCoordinates((int) x, (int) y);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                for (Kitten k : mKitties) {
                    k.handleActionUp((int) x, (int) y);
                    k.setTouched(false);
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setGamePieces(List<Kitten> kitties, Home home) {
        setKitties(kitties);
        setHome(home);
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (mDetector.onTouchEvent(event))
            return true;
        else
            return super.onTouchEvent(event); // or false (it's what you whant).
    }
    */

    public List<Kitten> getKitties() {
        return mKitties;
    }

    public void setKitties(List<Kitten> kitties) {
        this.mKitties = kitties;
    }

    public Home getHome() {
        return mHome;
    }

    public void setHome(Home mHome) {
        this.mHome = mHome;
    }

    class GameGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "GameView Gestures";

        /*
        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            for (Kitten k : mKitties) {
                k.handleActionDown((int) event.getX(), (int) event.getY());
            }
            return true;
        }
        */

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
            Log.d("Fling", "FLUNG" +
                    " X POS: " + Float.toString(e1.getY()) +
                    " Y POS: " + Float.toString(e1.getX()) +
                    " X VEL: " + Float.toString(velocityX) +
                    " Y VEL: " + Float.toString(velocityY));
            if (velocityY > 10000){
                Log.d("Fling", "Nice downswipe");
                for (Kitten k : mKitties) {
                    k.handleActionFlung(e1.getX(), e1.getY());
                }
            }
            return true;
        }
    }


}
