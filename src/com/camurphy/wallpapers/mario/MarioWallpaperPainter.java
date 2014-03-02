package com.camurphy.wallpapers.mario;

import com.camurphy.wallpapers.mario.scene.Scene;
import com.camurphy.wallpapers.mario.scene.OverworldScene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MarioWallpaperPainter extends Thread {

    private final SurfaceHolder mSurfaceHolder;

    private boolean mThreadWaiting = true, mThreadRunning;

    private long mPreviousTime, mSecondCounter;
    private int mFrameCounter, mLastSecondFrameCount = 0;

    private Scene mScene;

    private int mScreenHeight, mFpsTextHeight;

    private Paint mFpsRectPaint;

    public MarioWallpaperPainter(SurfaceHolder surfaceHolder, Context context) {
        mSurfaceHolder = surfaceHolder;

        // Don't animate until surface is created and displayed
        mThreadWaiting = true;

        // Default to overland scene
        mScene = new OverworldScene(context.getResources(), context.getPackageName());

        mFpsRectPaint = new Paint();
        Rect textBounds = new Rect();
        mFpsRectPaint.getTextBounds("000", 0, 1, textBounds);

        mFpsTextHeight = textBounds.height();
    }

    public void pausePainting() {
        mThreadWaiting = true;
        synchronized (this) {
            this.notify();
        }
    }

    public void resumePainting() {
        mThreadWaiting = false;
        synchronized (this) {
            this.notify();
        }
    }

    public void stopPainting() {
        mThreadRunning = false;
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void run() {

        mThreadRunning = true;
        Canvas c = null;

        while (mThreadRunning) {
            synchronized (this) {
                if (mThreadWaiting) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
            }

            try {
                c = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    render(c);
                }
            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setSurfaceSize(int width, int height) {
        mScreenHeight = height;

        mScene.setScreenSize(width, height);

        synchronized (this) {
            notify();
        }
    }

    public void doTouchEvent(MotionEvent event) {
        mThreadWaiting = false;

        synchronized (this) {
            notify();
        }
    }

    private void render(Canvas c) {

        long currentTime = System.currentTimeMillis();
        long elapsed = 0;

        if (mPreviousTime > 0) {
            elapsed = currentTime - mPreviousTime;
        }

        mSecondCounter += elapsed;

        if (mSecondCounter >= 1000) {
            while (mSecondCounter >= 1000) {
                mSecondCounter -= 1000;
            }

            mLastSecondFrameCount = mFrameCounter;
            mFrameCounter = 0;
        }

        mScene.render(c, elapsed);

        // Render FPS
        c.drawText(Integer.toString(mLastSecondFrameCount), 2, mScreenHeight - mFpsTextHeight,
                mFpsRectPaint);

        mPreviousTime = currentTime;

        mFrameCounter ++;
    }

    public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
            float yStep, int xPixels, int yPixels) {
        mScene.onOffsetChanged(xOffset);

        synchronized (this) {
            notify();
        }
    }
}