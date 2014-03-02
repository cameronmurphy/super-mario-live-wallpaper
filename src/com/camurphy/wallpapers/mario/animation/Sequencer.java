package com.camurphy.wallpapers.mario.animation;

import java.util.ArrayList;
import java.util.List;

import com.camurphy.wallpapers.mario.IObserver;

public class Sequencer {

    private int mFrameCount;

    private int mCurrentFrame;
    private long mElapsedTime;

    private int[] mFrameTimes;

    private List<IObserver> mObservers;

    public Sequencer(int frameRate, int frameCount) {
        mObservers = new ArrayList<IObserver>();

        mFrameCount = frameCount;
        mFrameTimes = new int[frameCount];

        // Calculate and store the frame times
        for (int i = 0; i < frameCount; i++) {
            // mFrameTimes represents the end time in milliseconds of each frame. The first frame (when i is 0) ends
            // after 1/frameRate seconds or 1000/frameRate milliseconds. The second frame ends at 2000/frameRate
            // milliseconds etc.
            mFrameTimes[i] = (1000 * (i + 1)) / frameRate;
        }
    }

    public boolean hasObservers() {
        return !mObservers.isEmpty();
    }

    public void updateElapsedTime(long elapsed) {

        mElapsedTime += elapsed;

        // Cache field lookups
        int[] frameTimes = mFrameTimes;
        int lastFrameTime = frameTimes[mFrameCount - 1];
        long elapsedTime = mElapsedTime;
        int frameCount = mFrameCount;
        int newFrame = 1;

        // If our millisecond counter has ventured past the end of the final frame (lastFrameTime), keep subtracting the
        // full length of the sequence until it is below this value. This caters for long pauses in rendering.
        if (elapsedTime > lastFrameTime) {
            while (elapsedTime > lastFrameTime) {
                elapsedTime -= lastFrameTime;
            }
        }

        // Loop through the frames of the sequence
        for (int i = 0; i < frameCount; i++) {
            // This block tests where our millisecond counter is in relation to our frame times. Seeing as our frame
            // times represent when each frame ends, this block does not execute when the millisecond counter is between
            // 0 and the end time of the first frame but this does not matter because newFrame defaults to 1.
            if ((elapsedTime > frameTimes[i]) && (elapsedTime <= frameTimes[i + 1])) {
                newFrame = i + 2;
            }
        }

        // When the frame changes notify the observers
        if (newFrame != mCurrentFrame) {
            List<IObserver> observers = mObservers;
            int observerCount = observers.size();

            for (int i = 0; i < observerCount; i++) {
                observers.get(i).update(newFrame);
            }

            mCurrentFrame = newFrame;
        }

        mElapsedTime = elapsedTime;
    }

    public void addObserver(IObserver observer) {
        mObservers.add(observer);
    }
}