package com.camurphy.wallpapers.mario.actor;

import com.camurphy.wallpapers.mario.IObserver;

public abstract class ActorState implements IObserver {

    protected Actor mActor;

    public void onEnter(Actor actor) {
        mActor = actor;
    }

    public void onExit(Actor actor) { }

    public void updateElapsedTime(long elapsed) { }

    // Prevent cloning
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        throw new CloneNotSupportedException();
    }

    public void updateScale(double scale) { }

    // TODO - Completely temporary
    public void setInitialPosition(int position) { }
}