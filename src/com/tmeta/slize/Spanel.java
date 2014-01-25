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

package com.tmeta.slize;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public abstract class Spanel extends Entity {
	
	protected static final int SWITCH_IN = 0;
	protected static final int SWITCH_OUT = 1;
	
	public interface SpListener {
		void switchInFinished(Spanel sp, Object payload);
		void switchOutFinish(Spanel sp, Object payload);
	}
	
	protected SpListener _listener;
	
	public void setListener(SpListener l) {
		_listener = l;
	}
	
	public SpListener getListener() {
		return _listener;
	}
	
	protected void notifyListener(int typ, Object payload) {
		if (_listener == null)
			return;
		switch (typ) {
		case SWITCH_IN:
			_listener.switchInFinished(this, payload);
			break;
		case SWITCH_OUT:
			_listener.switchOutFinish(this, payload);
			break;
		}
	}
	
	public abstract void create(GameActivity ga, Scene scene);
	public abstract void switchIn(Object payload);
	public abstract void switchOut(Object payload);
	public abstract boolean onBackgroundTouched(TouchEvent pSceneTouchEvent, float x, float y);
	public abstract boolean onBackpressed();
	public abstract void onPause();
}
