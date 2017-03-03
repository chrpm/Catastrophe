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
    private Path mPath;
    private Paint mPaint;

    private float mX, mY;
    private static final float TOLERANCE = 5;

    private Kitten mKitty;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // TODO: replace line paiting code with kitten drawing code
        Bitmap kittyPic = BitmapFactory.decodeResource(getResources(), R.drawable.cool_cat);
        mKitty = new Kitten(kittyPic, 300, 300);

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
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
        //canvas.drawPath(mPath, mPaint);
    }

    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        float difX = Math.abs(x - mX);
        float difY = Math.abs(y - mY);
        if (difX >= TOLERANCE || difY >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                break;
        }
        invalidate();
        return true;
    }
}
