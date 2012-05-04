package com.camurphy.wallpapers.mario.object;

import android.content.res.Resources;

public class BrickBox extends Object {

    public BrickBox(Resources res, String packageName) {
        super(res, packageName);
    }

    @Override
    protected String getResourceName() {
        return "overworld_box_brick";
    }

    @Override
    public void update(int state) { }
}