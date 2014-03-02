package com.camurphy.wallpapers.mario.scene;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.camurphy.wallpapers.mario.actor.QuestionMarkBox;
import com.camurphy.wallpapers.mario.animation.OverworldAnimator;
import com.camurphy.wallpapers.mario.object.BoxGroup;
import com.camurphy.wallpapers.mario.object.BrickBox;
import com.camurphy.wallpapers.mario.object.Grass;
import com.camurphy.wallpapers.mario.object.Hill;

public class OverworldScene extends Scene {

    public static final int LANDSCAPE_OFFSET_Y = -20;

    private static final int OBJECT_HILL_POSITION_X = 3;
    private static final int OBJECT_HILL_POSITION_Y = 145;

    private static final int OBJECT_GRASS_POSITION_X = 100;
    private static final int OBJECT_GRASS_POSITION_Y = 165;

    private static final int OBJECT_QUESTION_BOX_1_POSITION_X = 95;
    private static final int OBJECT_QUESTION_BOX_1_POSITION_Y = 117;

    private static final int OBJECT_BOX_GROUP_POSITION_X = 172;
    private static final int OBJECT_BOX_GROUP_POSITION_Y = 117;

    private int mGroundTileOffsetX;
    private Bitmap mGroundImage, mGroundImageScaled;
    private int mGroundTileCount;
    private int mGroundPositionY;

    public OverworldScene(Resources res, String packageName) {
        super(res, packageName);

        int groundImageId = res.getIdentifier("overworld_ground", "drawable", packageName);
        mGroundImage = BitmapFactory.decodeResource(res, groundImageId);

        OverworldAnimator animator = new OverworldAnimator();
        mAnimator = animator;

        Hill hill = new Hill(res, packageName);
        hill.setUnscaledPositionX(OBJECT_HILL_POSITION_X);
        hill.setUnscaledPositionY(OBJECT_HILL_POSITION_Y);
        mChildren.add(hill);

        Grass grass = new Grass(res, packageName);
        grass.setUnscaledPositionX(OBJECT_GRASS_POSITION_X);
        grass.setUnscaledPositionY(OBJECT_GRASS_POSITION_Y);
        mChildren.add(grass);
        animator.addGrassObject(grass);

        QuestionMarkBox questionMarkBox1 = new QuestionMarkBox(res, packageName);
        questionMarkBox1.setUnscaledPositionX(OBJECT_QUESTION_BOX_1_POSITION_X);
        questionMarkBox1.setUnscaledPositionY(OBJECT_QUESTION_BOX_1_POSITION_Y);
        mChildren.add(questionMarkBox1);
        animator.addQuestionMarkBox(questionMarkBox1);

        BoxGroup boxGroup = new BoxGroup(res, packageName);
        boxGroup.setUnscaledPositionX(OBJECT_BOX_GROUP_POSITION_X);
        boxGroup.setUnscaledPositionY(OBJECT_BOX_GROUP_POSITION_Y);

        boxGroup.addBox(new BrickBox(res, packageName));

        QuestionMarkBox box = new QuestionMarkBox(res, packageName);
        boxGroup.addBox(box);
        animator.addQuestionMarkBox(box);

        boxGroup.addBox(new BrickBox(res, packageName));

        box = new QuestionMarkBox(res, packageName);
        boxGroup.addBox(box);
        animator.addQuestionMarkBox(box);

        boxGroup.addBox(new BrickBox(res, packageName));

        mChildren.add(boxGroup);
    }

    @Override
    protected String getBackgroundResourceName() {
        return "overworld_bg";
    }

    @Override
    protected int getLandscapeOffsetY() {
        return LANDSCAPE_OFFSET_Y;
    }

    @Override
    public void onOffsetChanged(float xOffset) {
        super.onOffsetChanged(xOffset);

        // Calculates how many pixels to offset the ground tiles by.
        // This is found by calculating the remainder of the division of mStageOffsetX (how many pixels between stage
        // left and screen left) and the width of a ground tile.
        mGroundTileOffsetX = mStageOffsetX % mGroundImageScaled.getWidth();
    }

    @Override
    public void setScreenSize(int width, int height) {
        super.setScreenSize(width, height);

        // Calculate how many ground tiles will be necessary given the screen size
        mGroundTileCount = mScreenWidth / mGroundImageScaled.getWidth() + 1;

        // Calculates the Y coordinate of the ground
        mGroundPositionY = mBackgroundImageScaled.getHeight() - mGroundImageScaled.getHeight() - mScreenOffsetY;
    }

    @Override
    protected void scaleImagery(double scale) {
        super.scaleImagery(scale);
        mGroundImageScaled = Bitmap.createScaledBitmap(mGroundImage, (int)
                (mGroundImage.getWidth() * scale), (int) (mGroundImage.getHeight() * scale), true);
    }

    @Override
    public void render(Canvas c, long elapsed) {
        super.render(c, elapsed);

        // Cache field lookups for performance reasons
        // http://developer.android.com/guide/practices/design/performance.html
        int groundTileCount = mGroundTileCount;
        int groundTileOffsetX = mGroundTileOffsetX;
        int groundImageWidth = mGroundImageScaled.getWidth();
        int groundPositionY = mGroundPositionY;

        // Render ground
        for (int i = 0; i <= groundTileCount; i++) {
            c.drawBitmap(mGroundImageScaled, groundImageWidth * i - groundTileOffsetX,
                    groundPositionY, null);
        }
    }
}