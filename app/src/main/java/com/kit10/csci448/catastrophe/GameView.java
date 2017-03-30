package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kit10.csci448.catastrophe.model.Kitten;

import java.util.List;

/**
 * Created with help from JavaCodeGeeks CanvasView example
 */
public class GameView extends View {
    private Context context;
    public int width;
    public int height;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private List<Kitten> mKitties;

    public GameView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
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
                    k.setTouched(false);
                }
                break;
        }
        invalidate();
        return true;
    }

    public List<Kitten> getKitties() {
        return mKitties;
    }

    public void setKitties(List<Kitten> mKitties) {
        this.mKitties = mKitties;
    }
}
