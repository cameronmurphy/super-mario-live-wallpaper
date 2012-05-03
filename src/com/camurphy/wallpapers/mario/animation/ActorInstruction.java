package com.camurphy.wallpapers.mario.animation;

public abstract class ActorInstruction {
	protected long mDuration;
	
	public long getDuration() {
		return mDuration;
	}
	
	public void setDuration(long duration) {
		mDuration = duration;
	}
}