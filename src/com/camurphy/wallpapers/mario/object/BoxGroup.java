package com.camurphy.wallpapers.mario.object;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Canvas;

// Note: This class assumes all boxes in the game are the same dimensions

public class BoxGroup extends Object {

    private List<Object> mBoxCollection;

    public BoxGroup(Resources res, String packageName) {
        super(res, packageName);

        mBoxCollection = new ArrayList<Object>();
    }

    public void addBox(Object box) {
        if (mBoxCollection.size() == 0) {
            box.setUnscaledPositionX(mUnscaledPositionX);
        }

        box.setUnscaledPositionY(mUnscaledPositionY);
        mBoxCollection.add(box);
    }

    @Override
    public void updateScale(double scale) {
        List<Object> children = mBoxCollection;

        int childrenCount = children.size();

        // The change of scale will have changed the position and size of the first box in the group.
        int firstChildPositionX = -1;
        int boxWidth = -1;

        for (int i = 0; i < childrenCount; i++) {
            Object child = children.get(i);

            child.updateScale(scale);

            if (i == 0) {
                firstChildPositionX = child.getPositionX();
                boxWidth = child.getWidth();
            } else {
                child.setPositionX(firstChildPositionX + boxWidth * i);
            }
        }
    }

    @Override
    public void updateElapsedTime(long elapsed) {
        List<Object> children = mBoxCollection;

        int childrenCount = children.size();

        for (int i = 0; i < childrenCount; i++) {
            children.get(i).updateElapsedTime(elapsed);
        }
    }

    @Override
    public void render(Canvas c, long elapsed, int screenOffsetY, int stageOffsetX) {
        // TODO - Rendering code :)

        List<Object> children = mBoxCollection;

        int childrenCount = children.size();

        for (int i = 0; i < childrenCount; i++) {
            children.get(i).render(c, elapsed, screenOffsetY, stageOffsetX);
        }
    }

    @Override
    public void update(int state) { }
}