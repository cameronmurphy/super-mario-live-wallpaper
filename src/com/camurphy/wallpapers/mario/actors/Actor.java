package com.camurphy.wallpapers.mario.actors;

import android.content.res.Resources;

import com.camurphy.wallpapers.mario.objects.Object;

public abstract class Actor extends Object {
	
	public Actor(Resources res, String packageName) {
		super(res, packageName);
	}

	protected ActorState mState = null;
}