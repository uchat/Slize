/*
 * Copyright (C) 2014 tmeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.tmeta.slize.gameview;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;

import com.tmeta.slize.GameActivity;


public abstract class GameKeeper extends Entity {
	
	protected static final float WIDTH = 480;
	protected static final float HEIGHT = 15;
	
	protected GameActivity _game;
	
	protected float _fullTick;
	protected float mFirstWarning;
	protected float _tickLeft;
	protected boolean mWarning;
	
	protected boolean mRunning = false;
	protected TimeListener _listener;
	protected Rectangle mTimeBackground, mMask;
	protected Color bcolor = new Color(140f/255f, 186f/255f, 255f/255f);
	
	public interface TimeListener {
		void onTick(int tickLeft);
		void onTickAdded(float tickAdded);
		void onTimeExpired();
	}	
	
	public GameKeeper(GameActivity baseGame, TimeListener listener) {
		super(0, 0);
		_game = baseGame;
		mWarning = false;
		_listener = listener;
		
		mTimeBackground = new Rectangle(0, 0, WIDTH, HEIGHT, _game.getVertexBufferObjectManager());
		mTimeBackground.setColor(bcolor);
		attachChild(mTimeBackground);
		
		mMask = new Rectangle(0, 0, WIDTH, HEIGHT, _game.getVertexBufferObjectManager());
		mMask.setColor(203f/255f, 203f/255f, 203f/255f);
		mMask.setAlpha(0);
		attachChild(mMask);	
	}
	
	public void setFulltick(float fulltime, float firstWarning) {
		_fullTick = fulltime; // in second
		mFirstWarning = firstWarning;
		mMask.setAlpha(1);
	}

	public boolean isRunning() {
		return mRunning;
	}
	
	public void start() {	
		_tickLeft = _fullTick;
		mRunning = true;
	}
	
	public void resume() {
		mRunning = true;
	}
	
	public int getTickLeft() {
		return (int) _tickLeft;
	}
	
	public void pause() {
		mRunning = false;
	}
	
	public void hide() {
		mMask.registerEntityModifier(
				new SequenceEntityModifier(
						new DelayModifier(1.3f),
						new AlphaModifier(0.3f, 1, 0)));
		
		mTimeBackground.registerEntityModifier(
				new SequenceEntityModifier(
						new DelayModifier(1.3f),
						new AlphaModifier(0.3f, 1, 0)));
	}

	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		mMask.detachSelf();
		mMask.dispose();

		mTimeBackground.detachSelf();
		mTimeBackground.dispose();
		
		super.dispose();
	}
	
	public abstract void freqUpdate(float dt);
	public abstract void offbeatUpdate();
	public abstract float addTick(float t);	
}
