package com.camurphy.wallpapers.mario.object;

import android.content.res.Resources;

public class Hill extends Object {

    public Hill(Resources res, String packageName) {
        super(res, packageName);
    }

    @Override
    protected String getResourceName() {
        return "overworld_hill";
    }

    @Override
    public void update(int state) { }
}