package com.camurphy.wallpapers.mario.object;

import com.camurphy.wallpapers.mario.IObserver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.res.Resources;

/**
 * Represents an object on the screen.
 */
public abstract class Object implements IObserver {

    protected int mUnscaledPositionX, mPositionX, mUnscaledPositionY, mPositionY;

    private boolean explicitPositionX = false;

    public void setUnscaledPositionY(int position) {
        mUnscaledPositionY = position;
    }

    public int getUnscaledPositionY() {
        return mUnscaledPositionY;
    }

    public void setUnscaledPositionX(int position) {
        mUnscaledPositionX = position;
    }

    public void setPositionX(int position) {
        mPositionX = position;
        explicitPositionX = true;
    }

    public int getPositionY() {
        return mPositionY;
    }

    public int getPositionX() {
        return mPositionX;
    }

    public int getWidth() {
        return getImage().getWidth();
    }

    protected String getResourceName() {
        return null;
    }

    protected Bitmap mImage, mImageScaled;

    public Object(Resources res, String packageName) {
        if (getResourceName() != null) {
            // Cache sprite
            int resourceId = res.getIdentifier(getResourceName(), "drawable", packageName);

            mImage = BitmapFactory.decodeResource(res, resourceId);
        }
    }

    protected Bitmap getImage() {
        return mImageScaled;
    }

    public void updateScale(double scale) {
        if (mImage != null) {
            mImageScaled = Bitmap.createScaledBitmap(mImage, (int) (mImage.getWidth() *
                    scale), (int) (mImage.getHeight() * scale), true);
        }

        if (!explicitPositionX) {
            mPositionX = (int) (mUnscaledPositionX * scale);
        }

        mPositionY = (int) (mUnscaledPositionY * scale);
    }

    public void render(Canvas c, long elapsed, int screenOffsetY, int stageOffsetX) {
        Bitmap image = getImage();

        // TODO - Add logic to avoid rendering if the object is not visible
        c.drawBitmap(image, mPositionX - stageOffsetX, getPositionY() - screenOffsetY, null);
    }

    public void updateElapsedTime(long elapsed) { }
}