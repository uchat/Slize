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

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.LoopEntityModifier;

import com.tmeta.slize.GameActivity;

public class TimeBar extends GameKeeper {

	public TimeBar(GameActivity game, TimeListener timeListener) {
		super(game, timeListener);
	}

	@Override
	public void start() {	
		_tickLeft = _fullTick + 2;
		mRunning = true;
	}
	
	@Override
	public synchronized void freqUpdate(float dt) {
		if (! this.mRunning)
			return;
		
		_tickLeft -= 1;
		if (_tickLeft > _fullTick) {
			return;
		}
		
		if (_tickLeft <= 0) {
			pause();
			if (_listener != null)
				_listener.onTimeExpired();
			mRunning = false;
			_tickLeft = 0;
		}
		
		int tk = (int) (_tickLeft / _fullTick * WIDTH);
		mMask.setWidth(WIDTH - tk);
		mMask.setPosition(tk, 0);
		
		_listener.onTick((int)_tickLeft);
		
		if (_tickLeft == 0)
			return;
		
		if ((!mWarning) && _tickLeft <= mFirstWarning) {
			mWarning = true;
			mTimeBackground.setColor(239f/255f, 93f/255f, 66f/255f);
		}

		if (mWarning) {
			mTimeBackground.registerEntityModifier(new AlphaModifier(0.45f, 0.5f, 1));
			_game.sound.play(_game.sound.TIME);
		}
		
	}
	
	@Override
	public void offbeatUpdate() {
		;
	}

	@Override
	public synchronized float addTick(float t) {		
		
		mTimeBackground.registerEntityModifier(
				new LoopEntityModifier(
						new ColorModifier(0.3f,_game.res.COLOR[3], bcolor),
						3));
		
		float nt = _tickLeft + t;
		float ret = t;
		if (nt > _fullTick) {
			ret = _fullTick - _tickLeft;
			nt = _fullTick;
		}		
		_tickLeft = nt + 1;
		return ret;
	}
	
}
