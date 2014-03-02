package com.camurphy.wallpapers.mario.actor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class QuestionMarkBox extends Actor {

    private Bitmap mFrame1, mFrame1Scaled, mFrame2, mFrame2Scaled, mFrame3, mFrame3Scaled;

    private int mCurrentFrame = 1;

    private static final String IMAGE_FRAME_1_IDENTIFIER = "box_question_frame_1";
    private static final String IMAGE_FRAME_2_IDENTIFIER = "box_question_frame_2";
    private static final String IMAGE_FRAME_3_IDENTIFIER = "box_question_frame_3";

    public QuestionMarkBox(Resources res, String packageName) {
        super(res, packageName);

        // Cache sprites
        int frame1Id = res.getIdentifier(IMAGE_FRAME_1_IDENTIFIER, "drawable", packageName);
        int frame2Id = res.getIdentifier(IMAGE_FRAME_2_IDENTIFIER, "drawable", packageName);
        int frame3Id = res.getIdentifier(IMAGE_FRAME_3_IDENTIFIER, "drawable", packageName);

        mFrame1 = BitmapFactory.decodeResource(res, frame1Id);
        mFrame2 = BitmapFactory.decodeResource(res, frame2Id);
        mFrame3 = BitmapFactory.decodeResource(res, frame3Id);
    }

    @Override
    public void updateScale(double scale) {
        super.updateScale(scale);
        mFrame1Scaled = Bitmap.createScaledBitmap(
                mFrame1,
                (int) (mFrame1.getWidth() * scale),
                (int) (mFrame1.getHeight() * scale),
                true
        );

        mFrame2Scaled = Bitmap.createScaledBitmap(
                mFrame2,
                (int) (mFrame2.getWidth() * scale),
                (int) (mFrame2.getHeight() * scale),
                true
        );

        mFrame3Scaled = Bitmap.createScaledBitmap(
                mFrame3,
                (int) (mFrame3.getWidth() * scale),
                (int) (mFrame3.getHeight() * scale),
                true
        );
    }

    @Override
    protected Bitmap getImage() {

        switch (mCurrentFrame) {
        case 1:
            return mFrame1Scaled;
        case 2:
            return mFrame2Scaled;
        case 3:
            return mFrame3Scaled;
        }

        return null;
    }

    @Override
    public void update(int state) {
        mCurrentFrame = state;
    }
}