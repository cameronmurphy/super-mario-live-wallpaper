package com.camurphy.wallpapers.mario.scene;

import java.util.ArrayList;
import java.util.List;

import com.camurphy.wallpapers.mario.object.Object;
import com.camurphy.wallpapers.mario.animation.Animator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class Scene {
    /**
     * Double the width of the unscaled background image, to give the '3D' effect of the background moving slower
     * than the foreground when scrolling.
     */
    public static final int STAGE_WIDTH_UNSCALED = 424;

    public static final int ORIENTATION_PORTRAIT = 0;
    public static final int ORIENTATION_LANDSCAPE = 1;

    protected float mOffset;
    protected int mStageWidth;
    protected int mScreenOffsetY;

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Animator mAnimator;

    protected List<Object> mChildren;

    /** The number of pixels between the left side of the stage and the left side of screen */
    protected int mStageOffsetX;

    protected int mOrientation = ORIENTATION_PORTRAIT;

    private double mScale = 0;

    protected Bitmap mBackgroundImage;
    protected Bitmap mBackgroundImageScaled;

    private Rect mBackgroundSrcRect;
    private Rect mBackgroundDestRect;

    public Scene(Resources res, String packageName) {
        int backgroundImageId = res.getIdentifier(getBackgroundResourceName(), "drawable",
                packageName);
        mBackgroundImage = BitmapFactory.decodeResource(res, backgroundImageId);

        mChildren = new ArrayList<Object>();
    }

    protected abstract String getBackgroundResourceName();

    // landscapeOffsetY can be positive (pixels from the top) or negative (pixels from the bottom)
    protected abstract int getLandscapeOffsetY();

    protected void scaleImagery(double scale) {
        List<Object> children = mChildren;

        int childrenCount = children.size();

        for (int i = 0; i < childrenCount; i++) {
            children.get(i).updateScale(scale);
        }
    }

    public void render(Canvas c, long elapsed) {
        updateElapsedTime(elapsed);
        // Draw background
        c.drawBitmap(mBackgroundImageScaled, mBackgroundSrcRect, mBackgroundDestRect, null);
        renderChildren(c, elapsed);
    }

    private void updateElapsedTime(long elapsed) {
        List<Object> children = mChildren;

        int childrenCount = children.size();

        mAnimator.updateElapsedTime(elapsed);

        for (int i = 0; i < childrenCount; i++) {
            children.get(i).updateElapsedTime(elapsed);
        }
    }

    protected void renderChildren(Canvas c, long elapsed) {
        List<Object> children = mChildren;

        int childrenCount = children.size();

        int screenOffsetY = mScreenOffsetY;
        int stageOffsetX = mStageOffsetX;

        for (int i = 0; i < childrenCount; i++) {
            children.get(i).render(c, elapsed, screenOffsetY, stageOffsetX);
        }
    }

    public void onOffsetChanged(float xOffset) {
        mOffset = xOffset;

        // This calculates the left position of the view rectangle for the background. xOffset is 0 when we are at the
        // far left home screen, 0.5 when we are at the middle home screen and 1 when we are at the far right home
        // screen. This conveniently represents the percentage of our journey across the background image that should be
        // complete. What we're interested in is by how many pixels the view rectangle needs to be displaced. The
        // maximum displacement is obviously when our view rectangle is against the right side of the background image.
        // At this point, our displacement is the width of the background image minus the width of our rectangle. Our
        // rectangle is the size of the screen so mScreenWidth is appropriate. The result of this multiplied with
        // xOffset gives us the pixel displacement we were after.
        int viewRectLeft = (int) ((mBackgroundImageScaled.getWidth() - mScreenWidth) * xOffset);

        mBackgroundSrcRect = new Rect(viewRectLeft, mScreenOffsetY, viewRectLeft + mScreenWidth,
                mScreenOffsetY + mScreenHeight);

        // TODO - Explain
        mStageOffsetX = (int) (xOffset * (mStageWidth - mScreenWidth));
    }

    public void setScreenSize(int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;

        int backgroundWidth, backgroundHeight;
        double scale;

        // Calculate background image size to maintain aspect ratio and fill the screen
        // vertically in portrait
        if (height > width) {
            mOrientation = ORIENTATION_PORTRAIT;
            backgroundWidth = (int) (height * ((double) mBackgroundImage.getWidth() /
                    (double) mBackgroundImage.getHeight()));
            backgroundHeight = mScreenHeight;
            scale = (double) backgroundWidth / (double) mBackgroundImage.getWidth();
            mScreenOffsetY = 0;
        } else {
            mOrientation = ORIENTATION_LANDSCAPE;
            backgroundWidth = (int) (width * ((double) mBackgroundImage.getWidth() /
                    (double) mBackgroundImage.getHeight()));
            backgroundHeight = mScreenWidth;
            scale = (double) backgroundWidth / (double) mBackgroundImage.getWidth();

            int landscapeOffsetY = getLandscapeOffsetY();

            if (landscapeOffsetY >= 0) {
                mScreenOffsetY = (int) (landscapeOffsetY * scale);
            } else {
                // When landscapeOffsetY is negative we want to offset the view from the bottom of the screen and not
                // from the top. To calculate an offset that would place our view at the bottom we subtract the height
                // of the screen from the height of the background image. To offset the view by the desired amount we
                // invert the negative landscapeOffsetY value, scale it then subtract that.
                mScreenOffsetY = mBackgroundImageScaled.getHeight() - height - (int) (landscapeOffsetY * -1 * scale);
            }
        }

        if (scale != mScale) {
            mScale = scale;

            // Scale images
            mBackgroundImageScaled = Bitmap.createScaledBitmap(mBackgroundImage,
                    backgroundWidth, backgroundHeight, true);

            scaleImagery(scale);

            mStageWidth = (int) (STAGE_WIDTH_UNSCALED * scale);

            // Update the stage offset
            mStageOffsetX = (int) (mOffset * (mStageWidth - mScreenWidth));
        }

        // Create a default source rectangle for the background image
        mBackgroundSrcRect = new Rect(0, mScreenOffsetY, mScreenWidth, mScreenOffsetY + mScreenHeight);
        mBackgroundDestRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
    }
}