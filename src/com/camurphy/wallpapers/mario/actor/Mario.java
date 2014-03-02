package com.camurphy.wallpapers.mario.actor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// TODO - This should be a state machine. States will be assigned by at 'Director' or 'Script' class and they will
//        consume elapsed milliseconds as necessary, performing adjustments of Actor position and sprite and then they
//        will notify the Actor that a change of state is required and the next state must be retrieved somehow. If at
//        this point there is remaining time the next state must start performing its duties as necessary.
public class Mario extends Actor {
    public static final int FRAME_SMALL_STATIONARY = 1;
    public static final int FRAME_SMALL_RUNNING_1 = 2;
    public static final int FRAME_SMALL_RUNNING_2 = 3;
    public static final int FRAME_LARGE_STATIONARY = 3;
    public static final int FRAME_LARGE_RUNNING_1 = 4;
    public static final int FRAME_LARGE_RUNNING_2 = 5;

    public static final int STATE_STATIONARY = 1;
    public static final int STATE_RUNNING = 2;

    private Bitmap mLargeStationary, mLargeStationaryScaled, mLargeRunning1, mLargeRunning1Scaled,
            mLargeRunning2, mLargeRunning2Scaled;

    private static final String IMAGE_FRAME_LARGE_STATIONARY_IDENTIFIER = "mario_large_stationary";
    private static final String IMAGE_FRAME_LARGE_RUNNING_1_IDENTIFIER = "mario_large_running_1";
    private static final String IMAGE_FRAME_LARGE_RUNNING_2_IDENTIFIER = "mario_large_running_2";

    private int mCurrentFrame = 3;

    // TODO - Do not default to running
    private int mState = STATE_RUNNING;

    public Mario(Resources res, String packageName) {
        super(res, packageName);

        int largeStationaryId = res.getIdentifier(IMAGE_FRAME_LARGE_STATIONARY_IDENTIFIER, "drawable", packageName);
        int largeRunning1Id = res.getIdentifier(IMAGE_FRAME_LARGE_RUNNING_1_IDENTIFIER, "drawable", packageName);
        int largeRunning2Id = res.getIdentifier(IMAGE_FRAME_LARGE_RUNNING_2_IDENTIFIER, "drawable", packageName);

        mLargeStationary = BitmapFactory.decodeResource(res, largeStationaryId);
        mLargeRunning1 = BitmapFactory.decodeResource(res, largeRunning1Id);
        mLargeRunning2 = BitmapFactory.decodeResource(res, largeRunning2Id);
    }

    @Override
    public void updateScale(double scale) {
        super.updateScale(scale);
        mLargeStationaryScaled = Bitmap.createScaledBitmap(
                mLargeStationary,
                (int) (mLargeStationary.getWidth() * scale),
                (int) (mLargeStationary.getHeight()* scale),
                true
        );

        mLargeRunning1Scaled = Bitmap.createScaledBitmap(
                mLargeRunning1,
                (int) (mLargeRunning1.getWidth() * scale),
                (int) (mLargeRunning1.getHeight() * scale),
                true
        );

        mLargeRunning2Scaled = Bitmap.createScaledBitmap(
                mLargeRunning2,
                (int) (mLargeRunning2.getWidth() * scale),
                (int) (mLargeRunning2.getHeight() * scale),
                true
        );
    }

    @Override
    protected Bitmap getImage() {

        switch (mCurrentFrame) {
        case FRAME_LARGE_STATIONARY:
            return mLargeStationaryScaled;
        case FRAME_LARGE_RUNNING_1:
            return mLargeRunning1Scaled;
        case FRAME_LARGE_RUNNING_2:
            return mLargeRunning2Scaled;
        }

        return null;
    }

    @Override
    public void update(int state) {
        mCurrentFrame = state;
    }

    @Override
    public void updateElapsedTime(long elapsed) {
        // TODO - Tell the current state how much time has passed since the previous game loop iteration
    }
}