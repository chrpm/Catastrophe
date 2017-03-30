package com.kit10.csci448.catastrophe;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Adrien on 3/1/2017.
 */

public class Kitten {

    private int x;
    private int y;
    private Bitmap sweetCatPic;
    private boolean touched;

    public Kitten(Bitmap sweetCatPic, int x, int y) {
        this.x = x;
        this.y = y;
        this.sweetCatPic = sweetCatPic;
    }

    public Bitmap getSweetCatPic() {
        return sweetCatPic;
    }

    public void setSweetCatPic(Bitmap sweetCatPic) {
        this.sweetCatPic = sweetCatPic;
    }

    public int getX() {
        return x;
    }

    public void setCoordinates(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sweetCatPic, x - (sweetCatPic.getWidth() / 2), y - (sweetCatPic.getHeight() / 2), null);
    }

    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - sweetCatPic.getWidth() / 2) && (eventX <= (x + sweetCatPic.getWidth()/2))) {
            if (eventY >= (y - sweetCatPic.getHeight() / 2) && (y <= (y + sweetCatPic.getHeight() / 2))) {
                // Cat picture has been touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }
    }

    // possible future methods include move(), randomMovement(), flee(), etc.
    public void flee() {
        x += 10;
        y += 10;
    }
}
