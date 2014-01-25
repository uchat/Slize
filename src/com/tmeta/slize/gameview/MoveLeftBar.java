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

import com.tmeta.slize.GameActivity;

public class MoveLeftBar extends GameKeeper {
	
	public MoveLeftBar(GameActivity game, TimeListener timeListener) {
		super(game, timeListener);
	}

	@Override
	public void freqUpdate(float dt) {
		;	
	}
	
	@Override
	public void start() {	
		_tickLeft = _fullTick + 1;
		mRunning = true;
	}

	@Override
	public synchronized void offbeatUpdate() {
		if (! this.mRunning)
			return;
		
		_tickLeft -= 1;
		if (_tickLeft > _fullTick)
			return;
		
		
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
	}

	@Override
	public synchronized float addTick(float t) {
		float ret = _tickLeft + t;
		if (ret > _fullTick) {
			ret = _fullTick - _tickLeft;
		}		
		_tickLeft += ret;
		return ret;
	}
}
