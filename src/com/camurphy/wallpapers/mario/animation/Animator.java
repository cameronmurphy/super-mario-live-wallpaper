package com.camurphy.wallpapers.mario.animation;

import java.util.ArrayList;
import java.util.List;

public class Animator {

    protected List<Sequencer> mSequencers;

    public Animator()
    {
        mSequencers = new ArrayList<Sequencer>();
    }

    public void addSequencer(Sequencer sequencer) {
        mSequencers.add(sequencer);
    }

    public void updateElapsedTime(long elapsed) {
        List<Sequencer> sequencers = mSequencers;
        int sequencerCount = sequencers.size();

        for (int i = 0; i < sequencerCount; i++) {
            sequencers.get(i).updateElapsedTime(elapsed);
        }
    }
}