package com.camurphy.wallpapers.mario.actor.states;

import com.camurphy.wallpapers.mario.actor.Actor;
import com.camurphy.wallpapers.mario.actor.ActorState;
import com.camurphy.wallpapers.mario.actor.Mario;
import com.camurphy.wallpapers.mario.animation.Sequencer;

public class MarioLargeRunningActorState extends ActorState {

    private static ActorState sInstance;
    private Sequencer mSequencer;

    // TODO - Should state objects keep track of the scale?
    private double mScale;

    // TODO - Figure out the best way to keep track of his speed. For now, constant speed
    private int mSpeedX = 70;
    private double mSpeedScaledX;

    private int mStartPositionX = -30;

    private long mElapsed;

    private MarioLargeRunningActorState() {
        super();

        mSequencer = new Sequencer(12, 3);
    }

    @Override
    public void onEnter(Actor actor) {
        super.onEnter(actor);

        if (mSequencer.hasObservers() == false) {
            mSequencer.addObserver(this);
        }
    }

    @Override
    public void update(int state) {
        switch (state) {
        case 1:
            // First frame of mario running is the stationary image
            mActor.update(Mario.FRAME_LARGE_STATIONARY);
            break;
        case 2:
            mActor.update(Mario.FRAME_LARGE_RUNNING_1);
            break;
        case 3:
            mActor.update(Mario.FRAME_LARGE_RUNNING_2);
            break;
        }
    }

    @Override
    public void updateScale(double scale) {
        mScale = scale;

        if (mSpeedX > 0) {
            mSpeedScaledX = mSpeedX * scale;
        }
    }

    @Override
    public void updateElapsedTime(long elapsed) {
        mSequencer.updateElapsedTime(elapsed);

        mElapsed += elapsed;

        if (mElapsed >= 6200) {
            mElapsed = -100;
        }

        mActor.setPositionX(mStartPositionX + (int) (mElapsed * mSpeedScaledX / 1000));
    }
}