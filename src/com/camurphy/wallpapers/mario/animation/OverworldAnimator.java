package com.camurphy.wallpapers.mario.animation;

import com.camurphy.wallpapers.mario.actor.QuestionMarkBox;
import com.camurphy.wallpapers.mario.object.Grass;

public class OverworldAnimator extends Animator {

    private Sequencer mQuestionMarkBoxSequencer;
    private Sequencer mGrassSequencer;

    public OverworldAnimator() {
        super();

        mQuestionMarkBoxSequencer = new Sequencer(6, 3);
        addSequencer(mQuestionMarkBoxSequencer);

        mGrassSequencer = new Sequencer(3, 4);
        addSequencer(mGrassSequencer);
    }

    public void addQuestionMarkBox(QuestionMarkBox questionMarkBox) {
        mQuestionMarkBoxSequencer.addObserver(questionMarkBox);
    }

    public void addGrassObject(Grass grass) {
        mGrassSequencer.addObserver(grass);
    }
}