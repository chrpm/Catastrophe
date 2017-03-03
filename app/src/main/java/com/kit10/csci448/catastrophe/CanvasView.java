package com.kit10.csci448.catastrophe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created with help from JavaCodeGeeks CanvasView example
 */
public class CanvasView extends View {
    private Context context;
    public int width;
    public int height;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private float mX, mY;
    private static final float TOLERANCE = 5;

    private Kitten mKitty;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // TODO: replace line paiting code with kitten drawing code
        Bitmap kittyPic = BitmapFactory.decodeResource(getResources(), R.drawable.cool_cat);
        mKitty = new Kitten(kittyPic, 400, 400);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mKitty.draw(canvas);
    }

    public void clearCanvas() {
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mKitty.handleActionDown((int)x, (int)y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mKitty.isTouched()) {
                    // kitten is being moved
                    mKitty.setX((int)x);
                    mKitty.setY((int)y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mKitty.isTouched()) {
                    mKitty.setTouched(false);
                }
                break;
        }
        invalidate();
        return true;
    }
}
