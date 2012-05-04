package com.camurphy.wallpapers.mario.actor;

import android.content.res.Resources;

import com.camurphy.wallpapers.mario.object.Object;

public abstract class Actor extends Object {

    public Actor(Resources res, String packageName) {
        super(res, packageName);
    }

    protected ActorState mState = null;
}