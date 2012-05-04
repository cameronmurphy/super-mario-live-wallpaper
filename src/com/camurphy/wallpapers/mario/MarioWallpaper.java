package com.camurphy.wallpapers.mario;

import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws a rotating wireframe cube.
 */
public class MarioWallpaper extends WallpaperService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new MarioEngine();
    }

    class MarioEngine extends Engine {
        /** X-axis offset when rendering in landscape mode measured in pixels
         * of the original images */
        public static final int RENDERING_LANDSCAPE_X_OFFSET = 53;

        private MarioWallpaperPainter mPainter;

        MarioEngine() {
            SurfaceHolder holder = getSurfaceHolder();
            mPainter = new MarioWallpaperPainter(holder, getApplicationContext());
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mPainter.stopPainting();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                mPainter.resumePainting();
            } else {
                mPainter.pausePainting();
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            mPainter.setSurfaceSize(width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mPainter.start();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            boolean retry = true;
            mPainter.stopPainting();
            while (retry) {
                try {
                    mPainter.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep,
                int xPixels, int yPixels) {
            mPainter.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels, yPixels);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            mPainter.doTouchEvent(event);
        }
    }
}